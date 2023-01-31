drop table if exists contacts_phone cascade;
alter table contacts add phone varchar(16) not null;
alter table org_user alter column contacts_id set not null;
alter table friend alter column contacts_id set not null;