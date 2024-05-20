package com.example.JWT_Ejemplo_EMO.security.filter;

import com.example.JWT_Ejemplo_EMO.models.Usuario;
import com.example.JWT_Ejemplo_EMO.security.TokenJwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import com.example.JWT_Ejemplo_EMO.security.TokenJwtConfig.*;
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //Es necesario usar un AuthenticationManager y pasarselo a esta clase en el cosntructor

    private AuthenticationManager authenticationManager;

    //private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {
        /*En este metodo se recibe el JSON con las credenciales para el inicio de sesion
        * aqui se realiza el proceso de deserializacion para obtener el objeto Usuario*/
        Usuario usr = null;
        String username = null;
        String psw = null;


        try{
            /*Obtenemos un objeto tipo Usuario a partir del JSON recibido en el request*/
            usr = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
            username = usr.getUsername();
            psw = usr.getPsw();

        }catch(Exception e){
            System.out.println(e);
        }


        /*Creamos el objeto del tipo UsernamePasswordAuthenticationToken
        el cual guardara los valores de username y psw para iniciar el proceso
        de autenticacion con el objeto AuthenticationManager*/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, psw);

        return this.authenticationManager.authenticate(authenticationToken);
    }



    /*Con este metodo, al tener una autenticacion exitosa, genera un Token y lo devuelve en un response
    * Un JWT se compone de 3 partes, el header, el payload y el signature. Dentro de este metodo se firma el
    * token generado con l SECRET_KEY que se tiene arriba.*/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                           Authentication authResult) throws IOException, ServletException{

        User usr = (User) authResult.getPrincipal();
        String username = usr.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims().add("authorities",new ObjectMapper().writeValueAsString(roles)).build();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(TokenJwtConfig.SECRET_KEY)
                .compact();

        response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);

        Map<String, Object> json = new HashMap<>();
        json.put("token", token);
        json.put("username", username);
        json.put("message", String.format("Bienvenido %s, has iniciado sesion!!!", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(json));
        response.setContentType(TokenJwtConfig.CONTENT_TYPE);
        response.setStatus(200);
    }


    /*Metodo a ejecutar cuando la autenticacion fue insatisfactoria*/

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        Map<String, Object> json = new HashMap<>();
        json.put("message", "Error en la autenticacion, credenciales incorrectas...");
        json.put("error", failed.getMessage());


        response.getWriter().write(new ObjectMapper().writeValueAsString(json));
        response.setContentType(TokenJwtConfig.CONTENT_TYPE);
        response.setStatus(401);
    }
}
