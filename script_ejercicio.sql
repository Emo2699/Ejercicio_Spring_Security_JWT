/*Script para el ejemplo de JWT de Spring Security*/

CREATE DATABASE ejerciciojwt;

USE ejerciciojwt;

/*Creamos la tabla de usuarios*/
CREATE TABLE usuarios(
	id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    psw VARCHAR(60) NOT NULL,
    habilitado TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY(id)
);
ALTER TABLE usuarios ADD UNIQUE INDEX (username ASC) VISIBLE;
DESCRIBE usuarios;

/*Creamos la tabla de roles*/
CREATE TABLE roles(
	id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(45) NOT NULL,
    PRIMARY KEY(id)
);
DESCRIBE roles;
ALTER TABLE roles ADD UNIQUE INDEX (nombre ASC) VISIBLE;


/*Tabla intermedia debido a relacion muchos a muchos*/
CREATE TABLE usuarios_roles(
	usuario_id INT NOT NULL,
    rol_id INT NOT NULL,
	PRIMARY KEY(usuario_id, rol_id)
);
DESCRIBE usuarios_roles;

ALTER TABLE usuarios_roles ADD CONSTRAINT FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
ALTER TABLE usuarios_roles ADD CONSTRAINT FOREIGN KEY (rol_id) REFERENCES roles(id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;
 
/*Insertamos unos roles dentro de la base de datos :3*/
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');
INSERT INTO roles (nombre) VALUES ('ROLE_USER');



SELECT * FROM roles;
SELECT * FROM usuarios;
SELECT * FROM usuarios_roles;
