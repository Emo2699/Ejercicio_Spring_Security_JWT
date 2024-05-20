package com.example.JWT_Ejemplo_EMO.security;


import com.example.JWT_Ejemplo_EMO.security.filter.JwtAuthenticationFilter;
import com.example.JWT_Ejemplo_EMO.security.filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }


    /*Con esta clase podre devolver una psw cifrada con Spring Security*/
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*En esta clase se configuran todas las reglaa de seguridad a los endpoints*/
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Aqui determinamos a que rutas se tiene permiso o denegacion de acceso
        return http.authorizeHttpRequests((authz) -> authz
                        //Permitimos el acceso a "/api/v1/usuarios"
                        .requestMatchers(HttpMethod.GET,"/api/v1/usuarios").permitAll()
                        //Permitimos el acceso a "/api/v1/registrar"
                        .requestMatchers(HttpMethod.POST,"/api/v1/registrar").permitAll()
                        //las otras url las denegamos por solicitar autenticacion
                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                //Lo deshabilitamos ya que no nos conectaremos con un front
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
