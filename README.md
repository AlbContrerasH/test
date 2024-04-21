# Prueba tecnica
Segun lo solicitado, se crea proyecto que gestiona una tabla de usuarios y telefonos con generacion de token en JWT.

## Tecnologicas
- [Spring boot](https://spring.io/) - Creacoin del microservicio
- [Spring security](https://spring.io/projects/spring-security) - Encargado de la generacion del token
- [Lombok](https://projectlombok.org/) - Acortador de clases
- [H2 database](https://www.h2database.com/html/main.html) - Base de datos en memoria
- [Hibernate](https://hibernate.org/) - Persistencia
- [Gradle](https://gradle.org/) - Building
- [Java](https://www.oracle.com/cl/java/technologies/downloads/) - Lenguaje utilizado


## Caracteristicas

- Gestion de usuarios via REST
  -- Post (creacion)
  -- Delete (eliminacion)
  -- Get (listar usuario/s)
  -- Put (actualizar)
  -- patch (habilitar/deshabilitar)
- Validacion de Bearer token
- Test unitarios


Repositorio esta publico via GitHub.

## Ejecucion

Se requiere una version de java 17 o compatible.

Se ha precargado la base de datos con un usuario de prueba para generar el token para acceder al metodo POST de creacion de usuarios.
### CURL
-- Login generado por Spring Security
```sh
curl --location 'localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "root@test.cl",
    "password": "123Test#"
}'
```
-- Login customizado
```sh
curl --location --request PATCH 'localhost:8080/user/568c38b1-01dd-4ecb-b431-2fed9598030c'
```
-- Creacion
```sh
curl --location 'localhost:8080/user' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Usuario Prueba",
    "email": "test@abc.cl",
    "password": "123Test#",
    "phones": [{
        "number": 12345678,
        "cityCode": 9,
        "countryCode": 56
    }]
}'
```

-- Listar todos
```sh
curl --location 'localhost:8080/user'
```

-- Listar por uuid
```sh
curl --location 'localhost:8080/user/568c38b1-01dd-4ecb-b431-2fed9598030c'
```

-- Eliminacion
```sh
curl --location --request DELETE 'localhost:8080/user/568c38b1-01dd-4ecb-b431-2fed9598030c'
```
-- Actualizacion
```sh
curl --location --request PUT 'localhost:8080/user/568c38b1-01dd-4ecb-b431-2fed9598030c' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Usuario Prueba 2",
    "email": "test@abc.cl",
    "password": "123Test$",
    "phones": [{    
        "number": 12345678,
        "cityCode": 9,
        "countryCode": 56
    }]
}'
```

-- Desactivar usuario
```sh
curl --location --request PATCH 'localhost:8080/user/568c38b1-01dd-4ecb-b431-2fed9598030c'
```

#### Navegacion
Al iniciar la aplicacion, se creara el usuario para poder acceder al token JWT que sera necesario para la creacion, modificacion de los usuarios y eliminacion (POST, PUT, DELETE y PATCH).

Los metodos GET estan con libre acceso, incluyendo los accesos a los login.

