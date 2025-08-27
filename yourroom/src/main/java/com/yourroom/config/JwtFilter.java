package com.yourroom.config;

import com.yourroom.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// -----------------------------------------------------------------------------
// JWT FILTER
// -----------------------------------------------------------------------------

/**
 * Clase/filtro que intercepta cada petición HTTP para:
 *
 * - Extraer el token JWT del header Authorization (formato: Bearer <token>).
 * - Validar el token y, si es correcto, establecer la autenticación en el SecurityContext.
 * - Permitir el acceso sin autenticación a rutas que comienzan por /api/profile (excepción actual).
 * - Delegar siempre en el siguiente filtro con filterChain.doFilter(...).
 *
 * Ventajas / propósito:
 * - Centraliza la autenticación por token a nivel de filtro.
 * - Evita duplicar lógica de extracción/validación en los controladores.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    // -------------------------------------------------------------------------
    // DEPENDENCIAS
    // -------------------------------------------------------------------------
    private final JwtUtil jwtUtil;                 // Utilidades para leer/validar JWT
    private final UserDetailsService userDetailsService; // Carga de usuarios

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // -------------------------------------------------------------------------
    // FLUJO PRINCIPAL: doFilterInternal
    // -------------------------------------------------------------------------
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ---------------------------------------------------------------------
        // 1) EXCEPCIÓN DE RUTA (/api/profile)
        // ---------------------------------------------------------------------
        // Se permite el acceso a rutas que empiezan por /api/profile sin validar JWT.
        // Revisión futura: limitar la excepción a endpoints/verbos concretos si procede.
        String path = request.getRequestURI();
        if (path.startsWith("/api/profile")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ---------------------------------------------------------------------
        // 2) EXTRACCIÓN DEL TOKEN (Authorization: "Bearer <token>")
        // ---------------------------------------------------------------------
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.getUsernameFromToken(jwt);
        }

        // ---------------------------------------------------------------------
        // 3) CARGA DE USUARIO + 4) VALIDACIÓN DEL TOKEN
        // ---------------------------------------------------------------------
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                // -----------------------------------------------------------------
                // 5) PUBLICAR AUTENTICACIÓN EN EL CONTEXTO DE SEGURIDAD
                // -----------------------------------------------------------------
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // ---------------------------------------------------------------------
        // 6) CONTINUACIÓN DE LA CADENA DE FILTROS
        // ---------------------------------------------------------------------
        // Nota: este filtro no gestiona renovación del token; solo valida el actual.
        // Si se añaden más rutas públicas, conviene declararlas en SecurityFilterChain
        // en lugar de hacer excepciones locales aquí.
        filterChain.doFilter(request, response);
    }
}
