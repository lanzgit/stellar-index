-- Adicionar coluna updated_at nas tabelas
ALTER TABLE estrelas ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE planetas ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE luas ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE asteroides ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Criar trigger para atualizar automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Aplicar triggers
CREATE TRIGGER update_estrelas_updated_at BEFORE UPDATE ON estrelas
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_planetas_updated_at BEFORE UPDATE ON planetas
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_luas_updated_at BEFORE UPDATE ON luas
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_asteroides_updated_at BEFORE UPDATE ON asteroides
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Preencher valores iniciais para registros existentes
UPDATE estrelas SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
UPDATE planetas SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
UPDATE luas SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
UPDATE asteroides SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
