CREATE TABLE contacts (
    id bigserial primary key,
    address character varying(255)
);

CREATE TABLE contacts_email (
    contacts_id bigint references contacts(id),
    email character varying(255)
);

CREATE TABLE contacts_messengers (
    contacts_id bigint references contacts(id),
    messengers character varying(255)
);

CREATE TABLE contacts_phone (
    contacts_id bigint references contacts(id),
    phone character varying(255)
);

CREATE TABLE archive (
    id bigserial primary key,
    date_from date,
    incom real,
    spend real,
    date_to date,
    account_id bigint
);

CREATE TABLE org_user (
    id bigserial primary key,
    birthday date,
    login character varying(255),
    name character varying(255),
    password character varying(255),
    contacts_id bigint references contacts(id),
    uuid uuid
);

CREATE TABLE account (
    id bigserial primary key,
    amount real,
    currency character varying(255),
    name character varying(255),
    user_id bigint references org_user(id)
);

CREATE TABLE authority (
    id bigserial primary key,
    authority character varying(255),
    user_id bigint references org_user(id)
);

CREATE TABLE friend (
    id bigserial primary key,
    birthday date,
    name character varying(255),
    contacts_id bigint references contacts(id),
    user_id bigint references org_user(id),
    uuid uuid
);

CREATE TABLE transactions (
    id bigserial primary key,
    amount real,
    date_time timestamp without time zone,
    friend_id bigint,
    source_account bigint references account(id),
    target_account bigint references account(id)
);