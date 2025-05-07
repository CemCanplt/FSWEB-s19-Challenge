package com.twitter.mavikus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // BCryptPasswordEncoder dışında da yöntemler var.
        // Bcrpyt - SHA-256 - SHA-512 - Argon2 - PBKDF2 - SCrypt
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // csrf(AbstractHttpConfigurer::disable) = csrf(csrf -> csrf.disable())
        return httpSecurity.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // CORS yapılandırmasını aktif et
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/register/**", "/login/**").permitAll();
                    auth.requestMatchers("/tweet/**").authenticated();
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    // auth.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN");
                    // auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    // auth.requestMatchers(HttpMethod.GET, "/account/**").hasAnyAuthority("ADMIN", "USER");
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /*
    @Bean // CORS yapılandırması için yeni metod
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3200")); // React uygulamasının adresi
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 saat
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    */

    @Bean // Güvenliğin Veritanı ile çalıştığını sisteme söyler.
    public AuthenticationManager authManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }
}
