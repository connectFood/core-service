CREATE EXTENSION IF NOT EXISTS citext;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS core;

CREATE TABLE IF NOT EXISTS core.users (
  id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  uuid          UUID        NOT NULL DEFAULT gen_random_uuid(),
  full_name     VARCHAR(255) NOT NULL,
  email         CITEXT       NOT NULL,
  login         CITEXT       NOT NULL,
  password      VARCHAR(255) NOT NULL,
  roles         JSONB        NOT NULL DEFAULT '[]'::jsonb,
  created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version       INTEGER      NOT NULL DEFAULT 0
);

ALTER TABLE core.users
  ADD CONSTRAINT uq_users_uuid  UNIQUE (uuid),
  ADD CONSTRAINT uq_users_email UNIQUE (email),
  ADD CONSTRAINT uq_users_login UNIQUE (login);

CREATE INDEX idx_user_roles_gin ON core.users USING GIN (roles);


CREATE TABLE IF NOT EXISTS core.address (
  id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  uuid          UUID         NOT NULL DEFAULT gen_random_uuid(),
  user_id       BIGINT       NOT NULL REFERENCES core.users(id) ON DELETE CASCADE,
  street        VARCHAR(255) NOT NULL,
  number        VARCHAR(255) NOT NULL,
  complement    VARCHAR(255),
  neighborhood  VARCHAR(255),
  city          VARCHAR(255) NOT NULL,
  state         VARCHAR(255) NOT NULL,
  zip_code      VARCHAR(255) NOT NULL,
  country       VARCHAR(255) NOT NULL,
  address_type  VARCHAR(255),
  is_default    BOOLEAN      NOT NULL DEFAULT FALSE,
  created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
  version       INTEGER      NOT NULL DEFAULT 0
);

ALTER TABLE core.address
  ADD CONSTRAINT uq_address_uuid UNIQUE (uuid);

CREATE INDEX IF NOT EXISTS idx_address_user_id   ON core.address(user_id);
CREATE INDEX IF NOT EXISTS idx_address_zip_code  ON core.address(zip_code);

CREATE UNIQUE INDEX IF NOT EXISTS uq_address_user_default
  ON core.address(user_id)
  WHERE is_default IS TRUE;

CREATE OR REPLACE FUNCTION core.set_updated_at()
RETURNS trigger LANGUAGE plpgsql AS $$
BEGIN
  NEW.updated_at := NOW();
RETURN NEW;
END $$;

DROP TRIGGER IF EXISTS trg_users_set_updated_at ON core.users;
CREATE TRIGGER trg_users_set_updated_at
  BEFORE UPDATE ON core.users
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();

DROP TRIGGER IF EXISTS trg_address_set_updated_at ON core.address;
CREATE TRIGGER trg_address_set_updated_at
  BEFORE UPDATE ON core.address
  FOR EACH ROW EXECUTE FUNCTION core.set_updated_at();
