package dev.aceelv.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuration Two
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .successHandler(successHandler()) // URL a la que se redirige después del inicio de sesión exitoso
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionFixation()
                        .migrateSession()
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ALWAYS - IF_REQUIRED - NEVER - STATELESS
                        .invalidSessionUrl("/login") // Especifica la URL a la cual redirigir al usuario si se detecta una sesión inválida
                        .maximumSessions(1)// Permite configurar el número máximo de sesiones permitidas para un usuario.
                        .expiredUrl("/login") // Especifica la URL a la cual redirigir al usuario si su sesión ha expirado debido a la inactividad.
                        .sessionRegistry(sessionRegistry())
                )
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/session");
        });
    }
}
