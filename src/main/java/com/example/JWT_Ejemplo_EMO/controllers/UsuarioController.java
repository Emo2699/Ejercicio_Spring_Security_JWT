package com.example.JWT_Ejemplo_EMO.controllers;

import com.example.JWT_Ejemplo_EMO.models.Usuario;
import com.example.JWT_Ejemplo_EMO.services.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class UsuarioController {

    //Inyectamos el servicio
    @Autowired
    private UsuarioService usuarioServicio;


    @GetMapping("usuarios")
    public ResponseEntity<Map<String,Object>> listar(){
        try{
            Map<String,Object> json = new HashMap<>();
            json.put("data", this.usuarioServicio.findAll());
            json.put("status", HttpStatus.OK);

            return new ResponseEntity<>(json, HttpStatus.OK);
        }catch(Exception e){
            Map<String,Object> json = new HashMap<>();

            json.put("message", "Error en el servidor...");
            json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(json,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*Este metodo debe de ser privado para cualquier peticion, ya que permite la creacion
    * de usuarios con rol ADMIN*/
    @PostMapping("usuarios")
    public ResponseEntity<Map<String,Object>> agregarUsuario(@RequestBody Usuario usr){
        try{
            Map<String,Object> json = new HashMap<>();
            usr.setHabilitado(true);
            int resultado = this.usuarioServicio.guardarUsuario(usr);
            if(resultado == 1){
                json.put("message","Usuario registrado correctamente...");
                json.put("status", HttpStatus.CREATED);

                return new ResponseEntity<>(json, HttpStatus.CREATED);
            }else if(resultado == 0){
                json.put("message","Ya existe un usuario registrado con esa informacion");
                json.put("status", HttpStatus.BAD_REQUEST);

                return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
            }else{
                json.put("message", "Error en el servidor...");
                json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

                return new ResponseEntity<>(json,HttpStatus.INTERNAL_SERVER_ERROR);
            }
            /*
            Porcion de codigo del video sin validar duplicados
            Map<String,Object> json = new HashMap<>();
            usr.setHabilitado(true);
            json.put("data", this.usuarioServicio.save(usr));
            json.put("message","Usuario agregado correctamente...");
            json.put("status", HttpStatus.CREATED);

            return new ResponseEntity<>(json, HttpStatus.CREATED);

             */
        }catch(Exception e){
            Map<String,Object> json = new HashMap<>();

            json.put("message", "Error en el servidor...");
            json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(json,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*Este metodo puede ser publico para cualquier request ya que permite el registro
    * (agregar) cualquuier usuario al sistema, pero con la diferencia que unicamente
    * con el rol de Usuario normal, sin permisos de admin.*/
    @PostMapping("registrar")
    public ResponseEntity<Map<String,Object>> registrarUsuario(@RequestBody Usuario usr){
        try{
            Map<String,Object> json = new HashMap<>();
            usr.setAdmin(false);
            usr.setHabilitado(true);

            int resultado = this.usuarioServicio.guardarUsuario(usr);

            if(resultado == 1){
                json.put("message","Usuario registrado correctamente...");
                json.put("status", HttpStatus.CREATED);

                return new ResponseEntity<>(json, HttpStatus.CREATED);
            }else if(resultado == 0){
                json.put("message","Ya existe un usuario registrado con esa informacion");
                json.put("status", HttpStatus.BAD_REQUEST);

                return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
            }else{
                json.put("message", "Error en el servidor...");
                json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

                return new ResponseEntity<>(json,HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch(Exception e){
            Map<String,Object> json = new HashMap<>();

            json.put("message", "Error en el servidor...");
            json.put("status", HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(json,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
