package com.example.JWT_Ejemplo_EMO.repository;

import com.example.JWT_Ejemplo_EMO.models.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RolDAO extends CrudRepository<Rol, Long> {

    /*Cuando se agrega un usuario se le asigna un rol ya existente
    * dentro de la BD, por eso hay que buscar un rol en la tabla*/

    Optional<Rol> findByNombre(String name);

}
