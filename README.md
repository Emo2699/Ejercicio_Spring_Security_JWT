# Ejercicio Spring Security con JWT
## Base de Datos
Para este ejercicio se utilizó el gestor de Base de Datos MySQL, por lo que si se desea usar otro gestor con esta pequeña implementacion de Spring Security con JWT se tendrá que realizar
los cambios pertinentes para el gestor de la Base de Datos deseada.

La base de datos maneja tres tablas:
1. Tabla usuarios
2. Tabla roles
3. Tabla usuarios_roles (debido a la relacion N a N entre ambas tablas anteriores)

> [!TIP]
> Se recomienda crear la base de datos antes de levantar el proyecto ya que se proporciona el SQL para ello.

## Proyecto de Spring
Para el proyecto de Spring, la idea es que haya un endpoint publico con el cual se puede enlistar todos los usuarios registrados en el sistema.
y tambien hay un endpoint para agregar un usuario a la BD, sin embargo, este endpoint estará protegido con autenticación por token, por lo
se tendra que acceder al endpoint de login para iniciar sesion y que el sistema devuelva el Token con el cual el usuario ya podra consumir el 
recurso protegido sin ningun problema.

> [!IMPORTANT]
> Las clases de Spring Security para la configuracion del JWT deben ser estudiadas a profundidad para comprender su completo
> funcionamineto, aqui en el proyecto solo se da una breve explicacion sobre la finalidad de ciertas clases y metodos.
