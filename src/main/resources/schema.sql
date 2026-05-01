-- Crear la Base de Datos si no existe
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'CinemaRoyale')
BEGIN
    CREATE DATABASE CinemaRoyale;
END
GO

USE CinemaRoyale;
GO

-- =======================================================
-- ELIMINAR TABLAS SI EXISTEN (En orden inverso de dependencias)
-- =======================================================
DROP TABLE IF EXISTS Boleto;
DROP TABLE IF EXISTS EstadoAsiento;
DROP TABLE IF EXISTS Favorito;
DROP TABLE IF EXISTS Compra;
DROP TABLE IF EXISTS Cliente;
DROP TABLE IF EXISTS MetodoPago;
DROP TABLE IF EXISTS Funcion;
DROP TABLE IF EXISTS PeliculaGenero;
DROP TABLE IF EXISTS Asiento;
DROP TABLE IF EXISTS Sala;
DROP TABLE IF EXISTS Pelicula;
DROP TABLE IF EXISTS Clasificacion;
DROP TABLE IF EXISTS Genero;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Rol;
GO

-- =======================================================
-- CREACIÓN DE TABLAS Y RELACIONES
-- =======================================================

CREATE TABLE Rol (
    id_rol INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Usuario (
    id_usuario INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    activo BIT NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_Usuario_Rol FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
);

CREATE TABLE Genero (
    id_genero INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Clasificacion (
    id_clasificacion INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(10) NOT NULL,
    descripcion VARCHAR(100)
);

CREATE TABLE Pelicula (
    id_pelicula INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    duracion INT NOT NULL,
    id_clasificacion INT NOT NULL,
    descripcion VARCHAR(MAX),
    poster VARCHAR(255),
    creado_por INT NOT NULL,
    CONSTRAINT FK_Pelicula_Clasificacion FOREIGN KEY (id_clasificacion) REFERENCES Clasificacion(id_clasificacion),
    CONSTRAINT FK_Pelicula_Usuario FOREIGN KEY (creado_por) REFERENCES Usuario(id_usuario)
);

CREATE TABLE PeliculaGenero (
    id_pelicula INT NOT NULL,
    id_genero INT NOT NULL,
    PRIMARY KEY (id_pelicula, id_genero),
    CONSTRAINT FK_PeliculaGenero_Pelicula FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
    CONSTRAINT FK_PeliculaGenero_Genero FOREIGN KEY (id_genero) REFERENCES Genero(id_genero)
);

CREATE TABLE Sala (
    id_sala INT IDENTITY(1,1) PRIMARY KEY,
    numero_sala INT NOT NULL,
    capacidad INT NOT NULL
);

CREATE TABLE Asiento (
    id_asiento INT IDENTITY(1,1) PRIMARY KEY,
    id_sala INT NOT NULL,
    fila CHAR(1) NOT NULL,
    numero INT NOT NULL,
    CONSTRAINT UQ_Asiento UNIQUE (id_sala, fila, numero),
    CONSTRAINT FK_Asiento_Sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE Funcion (
    id_funcion INT IDENTITY(1,1) PRIMARY KEY,
    id_pelicula INT NOT NULL,
    id_sala INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    CONSTRAINT UQ_Funcion UNIQUE (id_sala, fecha, hora),
    CONSTRAINT FK_Funcion_Pelicula FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
    CONSTRAINT FK_Funcion_Sala FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE Cliente (
    id_cliente INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL UNIQUE,
    CONSTRAINT FK_Cliente_Usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

CREATE TABLE MetodoPago (
    id_metodo_pago INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Compra (
    id_compra INT IDENTITY(1,1) PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_metodo_pago INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT GETDATE(),
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',
    CONSTRAINT FK_Compra_Cliente FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    CONSTRAINT FK_Compra_MetodoPago FOREIGN KEY (id_metodo_pago) REFERENCES MetodoPago(id_metodo_pago)
);

CREATE TABLE Boleto (
    id_boleto INT IDENTITY(1,1) PRIMARY KEY,
    id_funcion INT NOT NULL,
    id_asiento INT NOT NULL,
    id_compra INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT UQ_Boleto UNIQUE (id_funcion, id_asiento),
    CONSTRAINT FK_Boleto_Funcion FOREIGN KEY (id_funcion) REFERENCES Funcion(id_funcion),
    CONSTRAINT FK_Boleto_Asiento FOREIGN KEY (id_asiento) REFERENCES Asiento(id_asiento),
    CONSTRAINT FK_Boleto_Compra FOREIGN KEY (id_compra) REFERENCES Compra(id_compra)
);

CREATE TABLE EstadoAsiento (
    id_estado INT IDENTITY(1,1) PRIMARY KEY,
    id_funcion INT NOT NULL,
    id_asiento INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',
    CONSTRAINT UQ_EstadoAsiento UNIQUE (id_funcion, id_asiento),
    CONSTRAINT FK_EstadoAsiento_Funcion FOREIGN KEY (id_funcion) REFERENCES Funcion(id_funcion),
    CONSTRAINT FK_EstadoAsiento_Asiento FOREIGN KEY (id_asiento) REFERENCES Asiento(id_asiento)
);

CREATE TABLE Favorito (
    id_favorito INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_pelicula INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT GETDATE(),
    CONSTRAINT UQ_Favorito UNIQUE (id_usuario, id_pelicula),
    CONSTRAINT FK_Favorito_Usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    CONSTRAINT FK_Favorito_Pelicula FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula)
);
GO

-- =======================================================
-- INSERCIÓN DE DATOS DE PRUEBA (ADMIN)
-- =======================================================

-- 1. Insertamos los roles básicos
INSERT INTO Rol (nombre) VALUES ('ADMIN');
INSERT INTO Rol (nombre) VALUES ('USER');

-- 2. Insertamos el usuario Admin
-- Nota: La contraseña está en texto plano temporalmente, 
-- pero en producción deberías usar el hash (ej. BCrypt) que espera tu backend.
INSERT INTO Usuario (username, nombre, email, password_hash, id_rol, activo) 
VALUES ('admin', 'Administrador General', 'admin@cinemaroyale.com', 'admin123', 1, 1);
GO