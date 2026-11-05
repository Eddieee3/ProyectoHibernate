-- ============================================================
-- Proyecto Hibernate - Script para pgAdmin
-- PASO 1: Ejecutar conectado a la base "postgres"
-- ============================================================
CREATE DATABASE proyecto_hibernate;

-- ============================================================
-- PASO 2: Conectarse a la base "proyecto_hibernate"
--         (en pgAdmin: clic en la base > Query Tool) y ejecutar:
-- ============================================================
CREATE SCHEMA IF NOT EXISTS catalogos;

CREATE TABLE IF NOT EXISTS catalogos.libro (
    id               BIGSERIAL      PRIMARY KEY,
    titulo           VARCHAR(150)   NOT NULL,
    autor            VARCHAR(120)   NOT NULL,
    anio_publicacion INTEGER        NOT NULL,
    precio           NUMERIC(10,2)  NOT NULL
);

-- Datos de prueba
INSERT INTO catalogos.libro (titulo, autor, anio_publicacion, precio) VALUES
    ('Cien años de soledad', 'Gabriel García Márquez', 1967, 450.00),
    ('Don Quijote de la Mancha', 'Miguel de Cervantes', 1605, 380.50),
    ('Azul...', 'Rubén Darío', 1888, 250.00);

SELECT * FROM catalogos.libro;
