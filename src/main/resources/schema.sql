-- Crear base de datos si no existe
IF DB_ID('CinemaRoyale') IS NULL
BEGIN
    CREATE DATABASE CinemaRoyale;
END
GO

USE CinemaRoyale;
GO

/* =========================
   TABLAS BASE (SIN DEPENDENCIAS)
========================= */

CREATE TABLE Rol (
    id_rol INT IDENTITY PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE MetodoPago (
    id_metodo_pago INT IDENTITY PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Genero (
    id_genero INT IDENTITY PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Clasificacion (
    id_clasificacion INT IDENTITY PRIMARY KEY,
    nombre VARCHAR(10) NOT NULL,
    descripcion VARCHAR(100)
);

CREATE TABLE Sala (
    id_sala INT IDENTITY PRIMARY KEY,
    numero_sala INT NOT NULL,
    capacidad INT NOT NULL
);

/* =========================
   USUARIOS
========================= */

CREATE TABLE Usuario (
    id_usuario INT IDENTITY PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    activo BIT NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT GETDATE(),

    FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
);

CREATE TABLE Cliente (
    id_cliente INT IDENTITY PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL,

    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

/* =========================
   PELÍCULAS
========================= */

CREATE TABLE Pelicula (
    id_pelicula INT IDENTITY PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    duracion INT NOT NULL,
    id_clasificacion INT NOT NULL,
    descripcion VARCHAR(MAX),
    poster VARCHAR(255),
    creado_por INT NOT NULL,

    FOREIGN KEY (id_clasificacion) REFERENCES Clasificacion(id_clasificacion),
    FOREIGN KEY (creado_por) REFERENCES Usuario(id_usuario)
);

CREATE TABLE PeliculaGenero (
    id_pelicula INT NOT NULL,
    id_genero INT NOT NULL,

    PRIMARY KEY (id_pelicula, id_genero),
    FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
    FOREIGN KEY (id_genero) REFERENCES Genero(id_genero)
);

/* =========================
   FUNCIONES Y ASIENTOS
========================= */

CREATE TABLE Funcion (
    id_funcion INT IDENTITY PRIMARY KEY,
    id_pelicula INT NOT NULL,
    id_sala INT NOT NULL,
    fecha_hora DATETIME2 NOT NULL,

    FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula),
    FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE Asiento (
    id_asiento INT IDENTITY PRIMARY KEY,
    id_sala INT NOT NULL,
    fila CHAR(1) NOT NULL,
    numero INT NOT NULL,

    UNIQUE (id_sala, fila, numero),
    FOREIGN KEY (id_sala) REFERENCES Sala(id_sala)
);

CREATE TABLE EstadoAsiento (
    id_estado INT IDENTITY PRIMARY KEY,
    id_funcion INT NOT NULL,
    id_asiento INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE',

    UNIQUE (id_funcion, id_asiento),
    FOREIGN KEY (id_funcion) REFERENCES Funcion(id_funcion),
    FOREIGN KEY (id_asiento) REFERENCES Asiento(id_asiento)
);

/* =========================
   COMPRAS Y BOLETOS
========================= */

CREATE TABLE Compra (
    id_compra INT IDENTITY PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_metodo_pago INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT GETDATE(),
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'COMPLETADA',

    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_metodo_pago) REFERENCES MetodoPago(id_metodo_pago)
);

CREATE TABLE Boleto (
    id_boleto INT IDENTITY PRIMARY KEY,
    id_funcion INT NOT NULL,
    id_asiento INT NOT NULL,
    id_compra INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',

    UNIQUE (id_funcion, id_asiento),
    FOREIGN KEY (id_funcion) REFERENCES Funcion(id_funcion),
    FOREIGN KEY (id_asiento) REFERENCES Asiento(id_asiento),
    FOREIGN KEY (id_compra) REFERENCES Compra(id_compra)
);

/* =========================
   FAVORITOS
========================= */

CREATE TABLE Favorito (
    id_favorito INT IDENTITY PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_pelicula INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT GETDATE(),

    UNIQUE (id_usuario, id_pelicula),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_pelicula) REFERENCES Pelicula(id_pelicula)
);

-- =======================================================
-- INSERCIÓN DE DATOS DE PRUEBA (SEED DATA)
-- =======================================================

-- 1. Roles
INSERT INTO Rol (nombre) VALUES ('ADMIN'), ('USER');

-- 2. Métodos de Pago
INSERT INTO MetodoPago (nombre) VALUES ('TARJETA'), ('EFECTIVO'), ('PAYPAL');

-- 3. Géneros
INSERT INTO Genero (nombre) VALUES ('Acción'), ('Drama'), ('Comedia'), ('Terror'), ('Ciencia Ficción');

-- 4. Clasificaciones
INSERT INTO Clasificacion (nombre, descripcion) VALUES 
('A', 'Todo público'),
('B', 'Mayores de 12 años'),
('B15', 'Mayores de 15 años'),
('C', 'Adultos únicamente');


-- 6. Usuarios
-- Nota: Contraseña hasheada (SHA-256) para 'admin123'
INSERT INTO Usuario (username, nombre, email, password_hash, id_rol, activo) VALUES 
('admin', 'Administrador General', 'admin@cinemaroyale.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 1, 1),
('usuario', 'Cliente Prueba', 'usuario@gmail.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 2, 1);



-- 8. Películas
INSERT INTO Pelicula (nombre, duracion, id_clasificacion, descripcion, poster, creado_por) VALUES 
('Avengers: Endgame', 181, 2, 'Los Vengadores restantes deben reunirse para deshacer el daño de Thanos.', 'https://m.media-amazon.com/images/M/MV5BMTc5MDE2ODcwNV5BMl5BanBnXkFtZTgwMzI2NzQ2NzM@._V1_.jpg', 1),
('The Joker', 122, 4, 'En Gotham City, Arthur Fleck, un payaso con problemas mentales, se embarca en una espiral de locura.', 'https://m.media-amazon.com/images/M/MV5BNGVjNWI4ZGUtNzE0MS00YTJmLWE0ZDctN2ZiYTk2YmI3NTYyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg', 1);

-- 9. Película Géneros
INSERT INTO PeliculaGenero (id_pelicula, id_genero) VALUES (1, 1), (1, 5), (2, 2);


GO
