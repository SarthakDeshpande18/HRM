package SRMS.FinalProject.Config;


import SRMS.FinalProject.JwtFilter.JwtFilter;
import SRMS.FinalProject.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authenticationProvider()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register", "/api/auth/verify-otp", "/api/auth/resend-otp", "/api/auth/login").permitAll()
                                .requestMatchers("/api/job/submit").hasAuthority("USER")
                                .requestMatchers("/api/job/update-status").hasAuthority("ADMIN")
                                .requestMatchers("/api/openings/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/api/technologies/**").hasAnyAuthority("USER", "ADMIN","INTERVIEWER")
                                .requestMatchers("/api/questions/**").hasAnyAuthority("INTERVIEWER", "ADMIN")
                                .requestMatchers("/api/job/all").hasAnyAuthority("INTERVIEWER", "ADMIN")
                                .requestMatchers("/api/questions/**").hasAnyAuthority("INTERVIEWER", "ADMIN")
                                .requestMatchers("/api/interviews/**").hasAnyAuthority("USER", "ADMIN","INTERVIEWER")
                                .requestMatchers("/api/interviews/**").hasAnyAuthority("USER", "ADMIN","INTERVIEWER")
                                .requestMatchers("/api/video/**").permitAll()
                                .requestMatchers("/api/video/create-room").permitAll()
                                .requestMatchers("/api/job/download/**").permitAll()
                                .requestMatchers("/api/profile/**").permitAll()
                                .requestMatchers("/uploads/**").permitAll()
                                .requestMatchers("/uploads/profile-pictures/**").permitAll()
                                .requestMatchers("/api/calendar/**").permitAll()
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService); // 👈 Use your CustomUserDetailsService
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    // This removes the "ROLE_" prefix
    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // no prefix
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
