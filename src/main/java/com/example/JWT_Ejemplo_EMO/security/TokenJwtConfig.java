package com.example.JWT_Ejemplo_EMO.security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJwtConfig {

    //Clase de constante que permite la creacion del token con sus tres partes
     public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
     public static final String PREFIX_TOKEN = "Bearer ";

     public static final String HEADER_AUTHORIZATION = "Authorization";
     public static final String CONTENT_TYPE = "application/json";


}
