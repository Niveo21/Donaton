-- Crea todas las bases de datos que usan los microservicios de Donaton.
-- Se ejecuta una sola vez, la primera vez que el contenedor de MySQL arranca.

CREATE DATABASE IF NOT EXISTS donaton_registro_db;
CREATE DATABASE IF NOT EXISTS donaton_donaciones_db;
CREATE DATABASE IF NOT EXISTS donaton_necesidad_db;
CREATE DATABASE IF NOT EXISTS donaton_notificacion_db;
CREATE DATABASE IF NOT EXISTS donaton_logistica_db;
CREATE DATABASE IF NOT EXISTS donaton_transport_db;
CREATE DATABASE IF NOT EXISTS donaton_acopio_db;
CREATE DATABASE IF NOT EXISTS donaton_pago_db;
