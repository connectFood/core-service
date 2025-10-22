DO $$
DECLARE
v_user_id BIGINT;
    v_user_roles JSONB := '["OWNER"]';
    -- Hash Bcrypt para a senha "123456"
    v_password_hash TEXT := '$2a$10$5KWvd29OvMeF3orAcYIdt.TfJ0xiburOm1njTy5UXhzsYJrgqElRK';

BEGIN
    RAISE NOTICE 'Inserindo usuário genérico (Professor FIAP)...';

INSERT INTO core.users (full_name, email, login, password, roles)
VALUES (
         'Professor Fiap',
         'professor.fiap@qa.exemplo.com',
         'professor.fiap',
         v_password_hash,
         v_user_roles
       )
  RETURNING id INTO v_user_id;

INSERT INTO core.address (
  user_id,
  street,
  number,
  complement,
  neighborhood,
  city,
  state,
  zip_code,
  country,
  address_type,
  is_default
)
VALUES (
         v_user_id,
         'Avenida Lins de Vasconcelos',
         '1222',
         'Prédio 1',
         'Aclimação',
         'São Paulo',
         'SP',
         '01538-001',
         'Brasil',
         'COMERCIAL',
         TRUE
       );

RAISE NOTICE 'Usuário Professor FIAP criado com sucesso (ID: %)', v_user_id;
END $$;
