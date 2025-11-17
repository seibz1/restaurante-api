# 🍽️ RESTAURANTE API - Sistema Híbrido de Gestão de Reservas

Este projeto implementa uma aplicação completa em arquitetura RESTful para gestão de reservas de um restaurante, cumprindo todos os requisitos de integração Híbrida (MySQL + MongoDB) e SQL Avançado (Triggers, Procedures, Views) do trabalho final.

---

## 1. ⚙️ Stack Tecnológica

| Componente | Tecnologia | Propósito no Projeto |
| :--- | :--- | :--- |
| **Backend** | Java 17+ (Spring Boot 3) | Lógica de negócio, API REST. |
| **Relacional (SQL)** | MySQL 8.0+ | Dados de Reserva, Usuário, e Rotinas Avançadas. |
| **Não-Relacional (NoSQL)** | MongoDB | Documentação do Cardápio (dados flexíveis). |
| **Frontend** | HTML/CSS/Vanilla JS | Interface de demonstração e consumo da API. |

## 2. 🚀 Instruções de Execução (Guia Rápido)

Para iniciar o sistema, siga a ordem abaixo:

### 2.1. Configuração dos Bancos de Dados

1.  **Pré-requisito:** Certifique-se de que os serviços do **MySQL** e **MongoDB** estão em execução.
2.  **Criação do Schema:** Abra o seu cliente SQL (IntelliJ Console ou Workbench).
3.  **Execute o Script:** Execute o arquivo **`setup_restaurante_db.sql`** (localizado na raiz do repositório). Este script cria o banco, todas as rotinas, índices e o usuário de aplicação.

    * **Usuário da Aplicação (Obrigatório):** `restaurante_app`
    * **Senha da Aplicação:** `blablabla123`

### 2.2. Inicialização do Backend (API)

1.  **Abrir:** Importe o projeto no IntelliJ IDEA como um projeto Maven.
2.  **Verificar Credenciais:** Confirme se o arquivo `application.properties` está usando o usuário limitado `restaurante_app` (requisito de segurança).
3.  **Rodar:** Execute a classe principal `RestauranteApiApplication.java`.
    * **Porta:** O servidor iniciará na porta **`8081`** (a 8080 foi alterada devido a conflitos ambientais).

### 2.3. Teste Funcional (Frontend)

1.  Com o backend rodando, abra o arquivo **`index.html`** no seu navegador.
2.  Clique em "Entrar" (Login Simulado).
3.  Acesse **`reserva.html`** e realize um teste de reserva para `2025-11-17T19:00`.

---

## 3. 🛡️ Resumo dos Requisitos Avançados (Justificativa)

| Recurso Avançado | Objeto Implementado | Justificativa no Projeto |
| :--- | :--- | :--- |
| **Acesso Não-Root** | Usuário `restaurante_app` | Cumpre o requisito de segurança, aplicando o princípio do menor privilégio. |
| **Procedures (1) e Functions (1)** | `sp_fazer_reserva` / `func_gerar_proximo_id_reserva` | Rotina `sp_fazer_reserva` centraliza a lógica crítica (conflito e capacidade) no SGBD, garantindo atomicidade e performance. |
| **Triggers (2)** | `trg_after_update_usuarios` e `trg_before_insert_reservas` | `trg_before_insert_reservas` atua como camada de segurança final, impedindo a violação da regra de capacidade mesmo em inserções diretas. |
| **Views (2)** | `vw_reservas_de_hoje` e `vw_relatorio_ocupacao_mesas` | Simplificam consultas complexas (`JOIN`s e `GROUP BY`) para relatórios gerenciais e consumo do backend. |
| **NoSQL Integration** | `ItemCardapio` (MongoDB) | Utilizado para modelar o Cardápio, aproveitando a flexibilidade do JSON para dados não-relacionais (ingredientes, opções). |

---

## 4. ⚠️ Desafio Documentado (Bug Conhecido - Apenas para Relatório)

O projeto enfrentou problemas persistentes de autenticação de ambiente (corrigidos via `mysql_native_password`) e de comunicação (resolvidos via `WebConfig.java` para CORS).

**Observação de Lógica de Negócio:**
Em ambientes específicos, a validação de **conflito de horário** pode, ocasionalmente, não retornar o erro esperado, permitindo uma reserva duplicada em horários sobrepostos, apesar da complexa lógica de `(InicioA < FimB) E (FimA > InicioB)` ter sido implementada de forma redundante no **Service** e na **Procedure** (`sp_fazer_reserva`). A causa principal é a manipulação de fusos horários (`LocalDateTime`) pelo SGBD.

---
