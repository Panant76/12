alter table org_user alter column uuid set not null;
alter table org_user alter column uuid set default gen_random_uuid();