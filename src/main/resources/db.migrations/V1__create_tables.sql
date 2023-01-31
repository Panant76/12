CREATE TABLE organizer.contacts (
    id bigserial primary key,
    address character varying(255)
);

CREATE TABLE organizer.contacts_email (
    contacts_id bigint references contacts(id),
    email character varying(255)
);

CREATE TABLE organizer.contacts_messengers (
    contacts_id bigint references contacts(id),
    messengers character varying(255)
);

CREATE TABLE organizer.contacts_phone (
    contacts_id bigint references contacts(id),
    phone character varying(255)
);

CREATE TABLE organizer.archive (
    id bigserial primary key,
    date_from date,
    incom real,
    spend real,
    date_to date,
    account_id bigint
);

CREATE TABLE organizer.org_user (
    id bigserial primary key,
    birthday date,
    login character varying(255),
    name character varying(255),
    password character varying(255),
    contacts_id bigint references contacts(id),
    uuid uuid
);

CREATE TABLE organizer.account (
    id bigserial primary key,
    amount real,
    currency character varying(255),
    name character varying(255),
    user_id bigint references org_user(id)
);

CREATE TABLE organizer.authority (
    id bigserial primary key,
    authority character varying(255),
    user_id bigint references org_user(id)
);

CREATE TABLE organizer.friend (
    id bigserial primary key,
    birthday date,
    name character varying(255),
    contacts_id bigint references contacts(id),
    user_id bigint references org_user(id),
    uuid uuid
);

CREATE TABLE organizer.transaction (
    id bigserial primary key,
    amount real,
    date_time timestamp without time zone,
    friend_id bigint,
    source_account bigint references account(id),
    target_account bigint references account(id)
);