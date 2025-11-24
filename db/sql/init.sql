CREATE SCHEMA IF NOT EXISTS healenium;

-- Set the default schema search path for the healenium_user
ALTER ROLE healenium_user SET search_path TO healenium, public;

-- Optionally, you can grant all privileges on the schema to the healenium_user
GRANT ALL ON SCHEMA healenium TO healenium_user;

