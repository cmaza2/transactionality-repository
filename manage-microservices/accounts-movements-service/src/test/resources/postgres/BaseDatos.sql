CREATE TABLE taccounts (
                           id_account bigserial  NOT NULL,
                           account_number varchar(255) NOT NULL,
                           account_type varchar(255) NOT NULL,
                           id_customer bigint NOT NULL,
                           initial_balance decimal(10,2) NOT NULL,
                           status boolean NOT NULL
) ;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla ttransactions
--

CREATE TABLE ttransactions (
                               id_transaction bigserial  NOT NULL,
                               balance decimal(15,2) NOT NULL,
                               date date NOT NULL,
                               transaction_type varchar(255) NOT NULL,
                               value decimal(38,2) NOT NULL,
                               id_account bigint NOT NULL
) ;


-- Índice de clave primaria (esto ya está hecho al definir la clave primaria en la tabla)
ALTER TABLE taccounts
    ADD CONSTRAINT taccounts_pkey PRIMARY KEY (id_account);

-- Índice único para el `account_number`
ALTER TABLE taccounts
    ADD CONSTRAINT taccounts_uk UNIQUE (account_number);

-- Clave primaria para `ttransactions`
ALTER TABLE ttransactions
    ADD CONSTRAINT ttransactions_pkey PRIMARY KEY (id_transaction);

-- Índice sobre la columna `id_account` en `ttransactions`
CREATE INDEX idx_id_account ON ttransactions(id_account);


-- Crear una clave foránea que hace referencia a `id_account` de la tabla `taccounts`
ALTER TABLE ttransactions
    ADD CONSTRAINT ttransactions_fk1 FOREIGN KEY (id_account) REFERENCES taccounts(id_account);

COMMIT;
