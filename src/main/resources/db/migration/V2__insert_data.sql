DO $$
DECLARE
first_names TEXT[] := ARRAY[
        'Miguel', 'Arthur', 'Gael', 'Heitor', 'Theo', 'Davi', 'Gabriel', 'Bernardo', 'Samuel', 'João', 'Enzo', 'Pedro', 'Lucas', 'Benício', 'Guilherme', 'Nicolas', 'Lorenzo', 'Joaquim', 'Rafael', 'Matheus',
        'Helena', 'Alice', 'Laura', 'Maria Alice', 'Sophia', 'Manuela', 'Maitê', 'Liz', 'Cecília', 'Isabella', 'Luísa', 'Isis', 'Júlia', 'Eloá', 'Lívia', 'Maria Luísa', 'Valentina', 'Heloísa', 'Antonella', 'Maria Clara'
    ];
    last_names TEXT[] := ARRAY[
        'Silva', 'Santos', 'Oliveira', 'Souza', 'Rodrigues', 'Ferreira', 'Alves', 'Pereira', 'Lima', 'Gomes', 'Costa', 'Ribeiro', 'Martins', 'Carvalho', 'Almeida', 'Fernandes', 'Dias', 'Moraes', 'Andrade', 'Correia'
    ];
    street_types TEXT[] := ARRAY['Rua', 'Avenida', 'Praça', 'Travessa', 'Alameda', 'Estrada', 'Rodovia'];
    street_names TEXT[] := ARRAY[
        'Tiradentes', 'Quinze de Novembro', 'Sete de Setembro', 'da Consolação', 'Paulista', 'Ipiranga', 'Dom Pedro II', 'Getúlio Vargas', 'Juscelino Kubitschek', 'Santos Dumont',
        'das Palmeiras', 'dos Girassóis', 'das Acácias', 'das Orquídeas', 'dos Pinheiros'
    ];
    neighborhoods TEXT[] := ARRAY[
        'Centro', 'Vila Mariana', 'Pinheiros', 'Copacabana', 'Ipanema', 'Boa Viagem', 'Savassi', 'Batel', 'Moinhos de Vento', 'Jardins', 'Liberdade', 'Barra da Tijuca', 'Pampulha'
    ];
    address_types TEXT[] := ARRAY['RESIDENCIAL', 'COMERCIAL', 'CASA DE CAMPO', 'APARTAMENTO DE PRAIA'];
    cities_states JSONB[] := ARRAY[
        '{"city": "São Paulo", "state": "SP", "zip_prefix": "01000"}', '{"city": "Rio de Janeiro", "state": "RJ", "zip_prefix": "20000"}',
        '{"city": "Belo Horizonte", "state": "MG", "zip_prefix": "30000"}', '{"city": "Salvador", "state": "BA", "zip_prefix": "40000"}',
        '{"city": "Curitiba", "state": "PR", "zip_prefix": "80000"}', '{"city": "Recife", "state": "PE", "zip_prefix": "50000"}',
        '{"city": "Porto Alegre", "state": "RS", "zip_prefix": "90000"}', '{"city": "Fortaleza", "state": "CE", "zip_prefix": "60000"}',
        '{"city": "Brasília", "state": "DF", "zip_prefix": "70000"}', '{"city": "Florianópolis", "state": "SC", "zip_prefix": "88000"}'
    ]::JSONB[];

    -- Variáveis de iteração
    v_user_id BIGINT;
    v_user_roles JSONB;
    v_first_name TEXT;
    v_last_name TEXT;
    v_full_name TEXT;
    v_email_login_prefix TEXT;
    v_address_info JSONB;
    -- Hash Bcrypt para a senha "123456"
    v_password_hash TEXT := '$2a$10$5KWvd29OvMeF3orAcYIdt.TfJ0xiburOm1njTy5UXhzsYJrgqElRK';

BEGIN
    RAISE NOTICE 'Iniciando a criação de 200 usuários';

FOR i IN 1..200 LOOP
        v_first_name := first_names[1 + floor(random() * array_length(first_names, 1))];
        v_last_name := last_names[1 + floor(random() * array_length(last_names, 1))];
        v_full_name := v_first_name || ' ' || v_last_name;
        v_email_login_prefix := lower(regexp_replace(v_first_name, '\s+', '', 'g') || '.' || regexp_replace(v_last_name, '\s+', '', 'g'));

        IF i % 2 = 0 THEN
            v_user_roles := '["CUSTOMER"]';
ELSE
            v_user_roles := '["OWNER"]';
END IF;

INSERT INTO core.users (full_name, email, login, password, roles)
VALUES (v_full_name, v_email_login_prefix || i || '@qa.exemplo.com', v_email_login_prefix || i, v_password_hash, v_user_roles)
  RETURNING id INTO v_user_id;

v_address_info := cities_states[1 + floor(random() * array_length(cities_states, 1))];
INSERT INTO core.address (
  user_id, street, number, complement, neighborhood, city, state, zip_code, country, address_type, is_default
)
VALUES (
         v_user_id,
         street_types[1 + floor(random() * array_length(street_types, 1))] || ' ' || street_names[1 + floor(random() * array_length(street_names, 1))],
         (1 + floor(random() * 3000))::text,
         'Apto ' || (1 + floor(random() * 20)) || '0' || (1 + floor(random() * 9)),
         neighborhoods[1 + floor(random() * array_length(neighborhoods, 1))],
         v_address_info->>'city', v_address_info->>'state', (v_address_info->>'zip_prefix') || '-' || LPAD(i::text, 3, '0'),
         'Brasil',
         address_types[1 + floor(random() * array_length(address_types, 1))],
         TRUE
       );

IF i % 5 = 0 THEN
            v_address_info := cities_states[1 + floor(random() * array_length(cities_states, 1))];
INSERT INTO core.address (user_id, street, number, neighborhood, city, state, zip_code, country, address_type, is_default)
VALUES (
         v_user_id,
         street_types[1 + floor(random() * array_length(street_types, 1))] || ' ' || street_names[1 + floor(random() * array_length(street_names, 1))],
         (1 + floor(random() * 1500))::text,
         neighborhoods[1 + floor(random() * array_length(neighborhoods, 1))],
         v_address_info->>'city', v_address_info->>'state', (v_address_info->>'zip_prefix') || '-' || LPAD((i+1000)::text, 3, '0'),
         'Brasil',
         address_types[2], -- Garante que seja 'COMERCIAL'
         FALSE
       );
END IF;
END LOOP;

    RAISE NOTICE 'Criação de 200 usuários com role única concluída com sucesso!';
END $$;
