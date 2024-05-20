package com.example.JWT_Ejemplo_EMO.services;

import com.example.JWT_Ejemplo_EMO.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuario {

    List<Usuario> findAll();

    Usuario save(Usuario usr);

    /*Metodo para agregar un usuario pero validacion XD*/
    int guardarUsuario(Usuario usr);

    Optional<Usuario> buscarUsuarioPorNombre(String username);

}
