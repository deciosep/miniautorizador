-- Criar database se não existir
CREATE DATABASE IF NOT EXISTS miniautorizador CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- Usar o database criado
USE miniautorizador;
-- Configurações de timezone
SET time_zone = '-03:00';
-- Mensagem de confirmação
SELECT 'Database miniautorizador criado com sucesso!' as status;