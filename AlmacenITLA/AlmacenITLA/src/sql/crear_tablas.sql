-- =====================================================
-- SCRIPT SQL — Base de Datos: almacenitlafinal
-- Servidor: Aiven Cloud MySQL
-- =====================================================

-- Crear tabla de usuarios (si no existe)
CREATE TABLE IF NOT EXISTS usuarios (
    idUser    INT           NOT NULL AUTO_INCREMENT,
    UserName  VARCHAR(50)   NOT NULL UNIQUE,
    Nombre    VARCHAR(100)  NOT NULL,
    Apellido  VARCHAR(100)  NOT NULL,
    Telefono  VARCHAR(20),
    Email     VARCHAR(100),
    Password  VARCHAR(255)  NOT NULL,
    PRIMARY KEY (idUser)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Crear tabla de productos (si no existe)
CREATE TABLE IF NOT EXISTS productos (
    idProducto        INT           NOT NULL AUTO_INCREMENT,
    NombreProducto    VARCHAR(150)  NOT NULL,
    MarcaProducto     VARCHAR(100)  NOT NULL,
    CategoriaProducto VARCHAR(100)  NOT NULL,
    PrecioProducto    INT           NOT NULL DEFAULT 0,
    StockProducto     INT           NOT NULL DEFAULT 0,
    PRIMARY KEY (idProducto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
