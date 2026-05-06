package com.project.backendapi.config

import com.project.backendapi.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:5173", "http://localhost:8080")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/webjars/**"
                    ).permitAll()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/uploads/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                    .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/bookings/**").permitAll()
                    .requestMatchers("/free-resources/**").permitAll()
                    .requestMatchers("/digital-products/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/comments/*/likes").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/comments/*/likes").permitAll()
                    .requestMatchers(HttpMethod.POST, "/posts/*/likes").permitAll()
                    .requestMatchers(HttpMethod.DELETE, "/posts/*/likes").permitAll()
                    .anyRequest().authenticated()
            }
            .headers { it.disable() }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
