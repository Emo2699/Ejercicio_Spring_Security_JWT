package com.example.JWT_Ejemplo_EMO.security.filter;

import com.example.JWT_Ejemplo_EMO.security.SimpleGrantedAuthorityJsonCreator;
import com.example.JWT_Ejemplo_EMO.security.TokenJwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class JwtValidationFilter extends BasicAuthenticationFilter {
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    /*Con este metodo se estara validando que el Token que se envie en el header de la peticion sea
    * correcto con el que se tiene registrado. Es en esta clase que se configura el acceso a los recursos
    * */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
      String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);

      if(header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)){
          chain.doFilter(request, response);
          return;
      }


      String token = header.replace(TokenJwtConfig.PREFIX_TOKEN,"");
      try{
        Claims claims = Jwts.parser().verifyWith(TokenJwtConfig.SECRET_KEY).build().parseSignedClaims(token).getPayload();
        String usename = claims.getSubject();
        //String usename2 = (String) claims.get("username");
        Object authoritiesClaims = claims.get("authorities");

          Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                  new ObjectMapper()
                          .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                          .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class)
          );

          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usename,null,
                  authorities);

          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          chain.doFilter(request, response);

      }catch (JwtException e){
          Map<String, Object> json = new HashMap<>();
          json.put("error", e.getMessage());
          json.put("message","El token es invalido...");

          response.getWriter().write(new ObjectMapper().writeValueAsString(json));
          response.setContentType(TokenJwtConfig.CONTENT_TYPE);
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          response.setContentType(TokenJwtConfig.CONTENT_TYPE);
      }

    }
}
