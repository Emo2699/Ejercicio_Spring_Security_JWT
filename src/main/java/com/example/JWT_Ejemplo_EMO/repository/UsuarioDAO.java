package com.example.JWT_Ejemplo_EMO.repository;

import com.example.JWT_Ejemplo_EMO.models.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioDAO extends CrudRepository<Usuario,Long> {

    Optional<Usuario> findByUsername(String username);

}
