alter table org_user ADD CONSTRAINT login_unique UNIQUE (login);
alter table org_user alter column login set not null;