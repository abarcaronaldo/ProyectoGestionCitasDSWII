-- Crea las 6 bases de datos del sistema (una por microservicio).
--
-- Uso manual (instalación local de PostgreSQL, sin Docker):
--   psql -U postgres -f crear-bases.sql
--
-- Uso con Docker: este mismo archivo se monta como /docker-entrypoint-initdb.d
-- en el contenedor de Postgres (ver backend/docker-compose.yml), que lo ejecuta
-- automáticamente solo la primera vez que el volumen de datos está vacío.
--
-- Nota: CREATE DATABASE no admite "IF NOT EXISTS" en PostgreSQL, y tampoco
-- puede ejecutarse dentro de un bloque de transacción (un DO $$...$$ no sirve
-- aquí). Si vuelves a correr este script contra una instancia que ya tiene
-- alguna de estas bases, psql reportará el error "la base de datos ya existe"
-- para esa línea puntual y seguirá con las demás (psql no aborta el script
-- completo por un error individual, salvo que se invoque con -v ON_ERROR_STOP=1).

CREATE DATABASE auth_db;
CREATE DATABASE paciente_db;
CREATE DATABASE doctor_db;
CREATE DATABASE citas_db;
CREATE DATABASE historial_db;
CREATE DATABASE reporte_db;
