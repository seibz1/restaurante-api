-- ####################################################################
-- # SCRIPT DE CONFIGURAÇÃO FINAL DO BANCO DE DADOS 'restaurante_db'
-- # Este script consolida a DDL, Índices, Views, Procedures, Triggers
-- # e Configuração de Acesso (Requisitos do Trabalho).
-- ####################################################################

-- 1. CRIAÇÃO E SELEÇÃO DO ESQUEMA
CREATE DATABASE IF NOT EXISTS restaurante_db;
USE restaurante_db;

-- 2. DDL DE TABELAS MANUAIS
-- Tabela de Auditoria: Criada manualmente para ser usada pelo Trigger de auditoria.
CREATE TABLE IF NOT EXISTS auditoria_usuarios (
                                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                                  id_usuario_afetado INT,
                                                  campo_alterado VARCHAR(50),
    valor_antigo VARCHAR(255),
    valor_novo VARCHAR(255),
    alterado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    alterado_por VARCHAR(100)
    );

-- 3. CRIAÇÃO DE ÍNDICES PARA OTIMIZAÇÃO (Requisito: Mínimo 1 Índice)
-- Justificativa: Aumentam a velocidade de busca (SELECT) nas colunas críticas.
-- O "IF EXISTS" evita o erro de duplicação que ocorreu no console.

-- Índice Composto: Otimiza a consulta de conflito de reservas (a mais importante).
CREATE INDEX IF NOT EXISTS idx_reserva_mesa_data ON reservas (mesa_id, data_hora_inicio);

-- Índice para Login: Otimiza a busca de usuários por email.
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuarios (email);

-- Índice para Busca: Otimiza a busca de mesas pelo seu número.
CREATE INDEX IF NOT EXISTS idx_mesa_numero ON mesas (numero_mesa);


-- 4. CRIAÇÃO DE VIEWS (Requisito: Mínimo 2 Views)

-- View 1: vw_reservas_de_hoje (Painel Gerencial)
-- Justificativa: Simplifica a consulta de reservas do dia, unindo dados de 3 tabelas
-- para permitir que o Backend consuma um relatório simples.
CREATE OR REPLACE VIEW vw_reservas_de_hoje AS
SELECT
    r.id AS reserva_id,
    r.data_hora_inicio,
    r.data_hora_fim,
    u.nome AS nome_cliente,
    u.email AS email_cliente,
    m.numero_mesa,
    r.numero_pessoas
FROM
    reservas r
        JOIN
    usuarios u ON r.usuario_id = u.id
        JOIN
    mesas m ON r.mesa_id = m.id
WHERE
    DATE(r.data_hora_inicio) = CURDATE()
ORDER BY
    r.data_hora_inicio ASC;

-- View 2: vw_relatorio_ocupacao_mesas (Relatório Analítico)
-- Justificativa: Agrega dados para fornecer o total de reservas por mesa,
-- sendo essencial para a tomada de decisão sobre o layout do restaurante.
CREATE OR REPLACE VIEW vw_relatorio_ocupacao_mesas AS
SELECT
    m.numero_mesa,
    m.capacidade,
    COUNT(r.id) AS total_de_reservas
FROM
    mesas m
        LEFT JOIN
    reservas r ON m.id = r.mesa_id
GROUP BY
    m.id, m.numero_mesa, m.capacidade
ORDER BY
    total_de_reservas DESC;


-- ####################################################################
-- # ROTINAS (FUNCTIONS E PROCEDURES) - Requisito: Mínimo 2 Rotinas
-- ####################################################################

DELIMITER $$

-- FUNCTION 1: func_gerar_proximo_id_reserva (Requisito: Geração de ID Customizada)
-- Justificativa: Gera IDs sequenciais por dia, em vez de um AUTO_INCREMENT global,
-- oferecendo um código de reserva mais amigável ao cliente.
CREATE FUNCTION IF NOT EXISTS func_gerar_proximo_id_reserva(p_data DATE)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE proximo_id INT;

SELECT
    COUNT(*) + 1
INTO
    proximo_id
FROM
    reservas
WHERE
    DATE(data_hora_inicio) = p_data;

RETURN proximo_id;
END$$

-- PROCEDURE 1: sp_fazer_reserva
-- Justificativa: A procedure executa a validação crítica (conflito e capacidade)
-- no SGBD, garantindo atomicidade, segurança (SELECT FOR UPDATE) e integridade total
-- dos dados, sendo a principal rotina do sistema.
CREATE PROCEDURE IF NOT EXISTS sp_fazer_reserva(
    IN p_usuario_id INT,
    IN p_mesa_id INT,
    IN p_data_hora_inicio DATETIME,
    IN p_numero_pessoas INT,
    IN p_duracao_horas INT
)
BEGIN
    DECLARE v_capacidade_mesa INT;
    DECLARE v_data_hora_fim DATETIME;
    DECLARE v_conflitos INT;

    SET v_data_hora_fim = p_data_hora_inicio + INTERVAL p_duracao_horas HOUR;

    -- Trava a linha da mesa
SELECT capacidade INTO v_capacidade_mesa
FROM mesas
WHERE id = p_mesa_id FOR UPDATE;

-- Validação 1: Capacidade
IF p_numero_pessoas > v_capacidade_mesa THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Número de pessoas excede a capacidade da mesa.';
END IF;

    -- Validação 2: Conflito de Horário
SELECT COUNT(*) INTO v_conflitos
FROM reservas
WHERE mesa_id = p_mesa_id
  AND data_hora_inicio < v_data_hora_fim
  AND data_hora_fim > p_data_hora_inicio;

IF v_conflitos > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Horário indisponível. Conflito de reserva detectado.';
END IF;

    -- Inserção da reserva
INSERT INTO reservas (usuario_id, mesa_id, data_hora_inicio, data_hora_fim, numero_pessoas)
VALUES (p_usuario_id, p_mesa_id, p_data_hora_inicio, v_data_hora_fim, p_numero_pessoas);

SELECT * FROM reservas WHERE id = LAST_INSERT_ID();

END$$


-- ####################################################################
-- # TRIGGERS (GATILHOS) - Requisito: Mínimo 2 Triggers
-- ####################################################################

-- TRIGGER 1: trg_after_update_usuarios (Auditoria)
-- Justificativa: Dispara APÓS qualquer alteração na tabela 'usuarios',
-- registrando quem alterou (CURRENT_USER()), o quê (campo) e os valores OLD/NEW
-- para fins de auditoria e segurança.
CREATE TRIGGER IF NOT EXISTS trg_after_update_usuarios
AFTER UPDATE ON usuarios
                            FOR EACH ROW
BEGIN
    DECLARE usuario_modificador VARCHAR(100);
    SET usuario_modificador = CURRENT_USER();

    IF OLD.nome <> NEW.nome THEN
        INSERT INTO auditoria_usuarios
            (id_usuario_afetado, campo_alterado, valor_antigo, valor_novo, alterado_por)
        VALUES
            (OLD.id, 'nome', OLD.nome, NEW.nome, usuario_modificador);
END IF;

    IF OLD.email <> NEW.email THEN
        INSERT INTO auditoria_usuarios
            (id_usuario_afetado, campo_alterado, valor_antigo, valor_novo, alterado_por)
        VALUES
            (OLD.id, 'email', OLD.email, NEW.email, usuario_modificador);
END IF;

END$$

-- TRIGGER 2: trg_before_insert_reservas (Restrição de Integridade Final)
-- Justificativa: Atua como a ÚLTIMA barreira de segurança. Impede inserções diretas
-- que violam a capacidade, garantindo que a regra de negócio não possa ser ignorada
-- por código não-autorizado ou bugs na aplicação.
CREATE TRIGGER IF NOT EXISTS trg_before_insert_reservas
BEFORE INSERT ON reservas
FOR EACH ROW
BEGIN
    DECLARE v_capacidade INT;

SELECT capacidade INTO v_capacidade
FROM mesas
WHERE id = NEW.mesa_id;

IF NEW.numero_pessoas > v_capacidade THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'TRIGGER: A reserva excede a capacidade da mesa.';
END IF;
END$$

DELIMITER ;


-- 5. CONFIGURAÇÃO DE USUÁRIOS E CONTROLE DE ACESSO (Requisito: Acesso Não-Root)
-- Este bloco garante que o acesso à API seja feito por um usuário limitado.

-- Cria ou garante a existência do usuário de aplicação.
-- NOTA: A senha 'blablabla123' deve ser a mesma usada em application.properties.
CREATE USER IF NOT EXISTS 'restaurante_app'@'localhost'
IDENTIFIED BY 'blablabla123';

-- Concede as permissões mínimas necessárias: CRUD e permissão para executar procedures.
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE
      ON restaurante_db.* TO 'restaurante_app'@'localhost';

-- Caso o usuário já existisse, mas com senha errada, forçamos a atualização.
ALTER USER 'restaurante_app'@'localhost'
IDENTIFIED BY 'blablabla123';

-- Aplica todas as permissões
FLUSH PRIVILEGES;

-- ####################################################################
-- # FIM DO SCRIPT DE CONFIGURAÇÃO
-- ####################################################################


DROP USER IF EXISTS 'restaurante_app'@'localhost';

CREATE USER 'restaurante_app'@'localhost'
    IDENTIFIED BY 'blablabla123';

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE
    ON restaurante_db.* TO 'restaurante_app'@'localhost';

FLUSH PRIVILEGES;