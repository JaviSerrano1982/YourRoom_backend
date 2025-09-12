package com.yourroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// -----------------------------------------------------------------------------
// SECURITY CONFIG
// -----------------------------------------------------------------------------

/**
 * Configuración central de Spring Security.
 *
 * Objetivos principales:
 * - Definir la cadena de filtros de seguridad (SecurityFilterChain).
 * - Establecer qué endpoints son públicos y cuáles requieren autenticación.
 * - Registrar el filtro JWT (JwtFilter) antes del filtro de usuario/contraseña.
 * - Desactivar CSRF para APIs stateless (cuando se usa JWT en vez de sesiones).
 */
@Configuration
public class SecurityConfig {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    private final JwtFilter jwtFilter; // Filtro que valida el token JWT en cada request

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    /**
     * Constructor para inyectar el filtro JwtFilter en la configuración de seguridad.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // -------------------------------------------------------------------------
    // BEAN: PASSWORD ENCODER
    // -------------------------------------------------------------------------
    /**
     * Define el bean de codificación de contraseñas.
     *
     * - Utiliza BCryptPasswordEncoder, un algoritmo seguro de hashing con salt.
     * - Se usará en el registro y autenticación de usuarios para verificar credenciales.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // -------------------------------------------------------------------------
    // BEAN: SECURITY FILTER CHAIN
    // -------------------------------------------------------------------------
    /**
     * Define la cadena de filtros y las reglas de autorización.
     *
     * Detalles clave:
     * - CSRF deshabilitado (API sin estado con JWT).
     * - Rutas públicas (permitAll):
     *   - /api/users/register
     *   - /api/users/login
     *   - /api/profile/**
     * - El resto de rutas requieren autenticación.
     * - Se añade JwtFilter antes de UsernamePasswordAuthenticationFilter para
     *   que el contexto de seguridad se establezca a partir del token JWT.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
