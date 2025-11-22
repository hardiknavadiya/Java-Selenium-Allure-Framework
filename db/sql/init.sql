-- Healenium PostgreSQL initialization script
-- This script is mounted into the Postgres container at
-- /docker-entrypoint-initdb.d/init.sql by docker-compose.
-- It will run automatically on first container startup.

-- Create dedicated schema for Healenium (matches SPRING_POSTGRES_SCHEMA=healenium)
CREATE SCHEMA IF NOT EXISTS healenium;

-- Set the default schema search path for the healenium_user
ALTER ROLE healenium_user SET search_path TO healenium, public;

-- Note:
-- The Healenium backend (image healenium/hlm-backend:3.4.6) uses Flyway/liquibase
-- style migrations to create and evolve its own tables inside the configured schema.
-- As long as the schema exists and the user has rights, no additional DDL is required
-- here; the backend will create all required tables on first startup.

-- Optionally, you can grant all privileges on the schema to the healenium_user
GRANT ALL ON SCHEMA healenium TO healenium_user;

