/**
 * =======================================================
 * LÓGICA DO FRONTEND (VANILLA JAVASCRIPT)
 * Versão Final: 3 Páginas (Login, Admin, Reserva)
 * =======================================================
 */

const API_URL = 'http://localhost:8080/api';
const outputDiv = document.getElementById('output');
const currentPath = window.location.pathname;
let userId = 1; // Simulação de ID de usuário logado

// -----------------------------------------------------------------
// FUNÇÕES UTILITÁRIAS
// -----------------------------------------------------------------

function showMessage(message, isError = false) {
    const icon = isError ? '❌' : '✅';
    outputDiv.innerHTML = `<span style="font-size: 1.1em; font-weight: bold;">${icon} ${message}</span>`;
    outputDiv.className = isError ? 'message error' : 'message success';
}

async function apiCall(endpoint, method, data = null) {
    try {
        const response = await fetch(`${API_URL}${endpoint}`, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: data ? JSON.stringify(data) : null
        });

        if (response.status >= 500) {
            const errorData = await response.json();
            const rawMessage = errorData.message.split('.')[0];
            const cleanMessage = rawMessage.replace('java.lang.RuntimeException:', '').trim();
            showMessage(`FALHA NA LÓGICA: ${cleanMessage}.`, true);
            return null;
        }

        return response;

    } catch (error) {
        showMessage(`Erro de Conexão: O servidor Spring (porta 8080) pode estar offline. Detalhes: ${error.message}`, true);
        return null;
    }
}

// -----------------------------------------------------------------
// FUNÇÃO LOAD MESAS (Usada na página de Reserva)
// -----------------------------------------------------------------

async function loadMesas() {
    const mesasTableBody = document.querySelector('#mesasTable tbody');
    mesasTableBody.innerHTML = '<tr><td colspan="3">Buscando dados da API...</td></tr>';

    const response = await apiCall('/mesas', 'GET');
    if (!response) return;

    if (response.ok) {
        const mesas = await response.json();
        mesasTableBody.innerHTML = '';

        if (mesas.length === 0) {
            mesasTableBody.innerHTML = '<tr><td colspan="3">Nenhuma mesa cadastrada. Use o Painel Admin.</td></tr>';
        }

        mesas.forEach(mesa => {
            const row = mesasTableBody.insertRow();
            row.insertCell().textContent = mesa.id;
            row.insertCell().textContent = `Mesa ${mesa.numeroMesa}`;
            row.insertCell().textContent = `${mesa.capacidade} lugares`;
        });

        showMessage(`✅ Mesas carregadas (${mesas.length} itens). Pronto para reservar.`, false);
    } else {
        showMessage(`❌ Erro ao carregar mesas. Status: ${response.status}`, true);
    }
}

// -----------------------------------------------------------------
// LÓGICA DE INICIALIZAÇÃO DE PÁGINAS
// -----------------------------------------------------------------

document.addEventListener('DOMContentLoaded', function() {

    // --- LÓGICA PARA A PÁGINA ADMIN (admin.html) ---
    if (currentPath.includes('admin.html')) {

        // [EVENTO 1]: Cadastrar Usuário (POST /api/usuarios)
        document.getElementById('usuarioForm').addEventListener('submit', async function(e) {
            e.preventDefault();

            const userData = {
                nome: document.getElementById('userName').value,
                email: document.getElementById('userEmail').value,
                senha: document.getElementById('userSenha').value
            };

            const response = await apiCall('/usuarios', 'POST', userData);
            if (response && response.status === 201) {
                const data = await response.json();
                showMessage(`USUÁRIO CRIADO! ID: ${data.id}, Nome: ${data.nome}.`, false);
            }
        });

        // [EVENTO 2]: Cadastrar Mesa (POST /api/mesas)
        document.getElementById('mesaForm').addEventListener('submit', async function(e) {
             e.preventDefault();
             const mesaData = {
                 numeroMesa: parseInt(document.getElementById('mesaNumero').value),
                 capacidade: parseInt(document.getElementById('mesaCapacidade').value)
             };
             const response = await apiCall('/mesas', 'POST', mesaData);
             if (response && response.status === 201) {
                 const data = await response.json();
                 showMessage(`MESA CRIADA! ID: ${data.id}, Número: ${data.numeroMesa}.`, false);
             }
        });
    }

    // --- LÓGICA PARA A PÁGINA DE RESERVA (reserva.html) ---
    else if (currentPath.includes('reserva.html')) {

        loadMesas(); // Carregamento inicial das mesas

        document.getElementById('reservaForm').addEventListener('submit', async function(e) {
            e.preventDefault();

            const reservaData = {
                usuarioId: userId,
                mesaId: parseInt(document.getElementById('reservaMesaId').value),
                dataHoraInicio: document.getElementById('dataHoraInicio').value + ':00',
                numeroPessoas: parseInt(document.getElementById('numPessoas').value)
            };

            const response = await apiCall('/reservas', 'POST', reservaData);
            if (!response) return;

            if (response.status === 201) {
                const data = await response.json();
                const successMsg = `RESERVA SUCESSO! <br>ID da Reserva: ${data.id}. <br>Mesa: ${data.mesa.numeroMesa}. <br>Início: ${data.dataHoraInicio.substring(11, 16)} | Fim: ${data.dataHoraFim.substring(11, 16)}`;
                showMessage(successMsg, false);
            }
        });

        // Anexa o listener ao botão "Carregar Mesas"
        document.querySelector('button[onclick="loadMesas()"]').addEventListener('click', loadMesas);
    }

    // --- LÓGICA PARA A PÁGINA DE LOGIN (index.html) ---
    else if (currentPath.includes('index.html')) {
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            e.preventDefault();
            showMessage('Login bem-sucedido. Redirecionando...', false);
            setTimeout(() => {
                window.location.href = 'reserva.html';
            }, 800);
        });
    }

}); // Fim do DOMContentLoaded