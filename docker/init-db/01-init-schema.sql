-- The application connects with currentSchema=iwanit, which does not exist
-- by default in a fresh PostgreSQL database. Create it on first boot so
-- Hibernate (ddl-auto=update) can create tables inside it.
CREATE SCHEMA IF NOT EXISTS iwanit;
