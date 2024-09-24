package com.example.myappjwt.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.myappjwt.filter.JwtAuthenticationFilter;
import com.example.myappjwt.service.impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
    CorsConfigurationSource corsSource(){
        CorsConfiguration configurationSource = new CorsConfiguration();
        configurationSource.setAllowedOrigins(Arrays.asList("*"));
        configurationSource.setAllowedMethods(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configurationSource);

        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter, CorsConfigurationSource corsSource) throws Exception {
    	return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/user/**").permitAll();
                    request.requestMatchers(HttpMethod.GET,"/user/**").permitAll();
                    request.requestMatchers(HttpMethod.POST,"/user/**").permitAll();
//                    request.requestMatchers("/product").hasRole("ADMIN");
                    request.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
//    	http
//        .csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(auth -> 
//            auth.requestMatchers("/user/**").permitAll()
//                .anyRequest().authenticated())
//        .exceptionHandling(exception -> 
//            exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
//        .sessionManagement(session -> 
//            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .httpBasic(Customizer.withDefaults());
//
//    	http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//    
//    	return http.build();
    }
    	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	    AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
	    authenticationManagerBuilder.userDetailsService(customUserDetailsService);
	    return authenticationManagerBuilder.build();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}