package com.Serah.DoctorAppointmentSystem.security;


import com.Serah.DoctorAppointmentSystem.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
   JwtAuthenticationEntryPoint AuthenticationEntryPoint;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize->
                        authorize
                                .requestMatchers(HttpMethod.POST,"/api/doctor/create").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/doctor/signin").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/doctor/resetpassword").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET,"/api/doctor/getDoctorByEmail").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET,"/api/doctor/getAllDoctors").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.POST,"/api/patient/signup").permitAll()
                                .requestMatchers("/api/patient/signin/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api/patient/resetPassword").hasRole("PATIENT")
                                .requestMatchers(HttpMethod.GET,"/api/patient/getPatientByEmail").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET,"/api/patient/getAllPatients").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.POST,"/api/patient/updatePatient").hasAnyRole("PATIENT,DOCTOR")
                                .requestMatchers(HttpMethod.POST,"/api/appointment/bookAppointment").hasRole("PATIENT")
                                .requestMatchers(HttpMethod.GET,"/api/appointment/getAllAppointments").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET,"/api/appointment/{id}").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET,"/api/appointment/{id}/completed").hasRole("DOCTOR")
                                .requestMatchers(HttpMethod.GET, "/api/appointment/User").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/appointment/fulfilled").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/appointment/unFulfilled").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api/appointment/{id}").permitAll()
                                .requestMatchers("/api/doctor/get").hasRole("PATIENT")
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




}
