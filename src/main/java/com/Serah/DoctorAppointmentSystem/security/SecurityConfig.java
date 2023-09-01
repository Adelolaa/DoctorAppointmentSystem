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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private UserDetails userDetails;

    public SecurityConfig(UserDetails userDetails){
        this.userDetails= userDetails;
    }

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize->
                        authorize
                                .requestMatchers(HttpMethod.POST,"/api/doctor/create").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/doctor/signin").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/doctor/resetpassword").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/doctor/getDoctorByEmail").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/doctor/getAllDoctors").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/patient/signup").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/patient/sigin").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api/patient/resetPassword").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/patient/getPatientByUsername").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/patient//getAllPatients").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/appointment/createAppointment").permitAll()
                                .requestMatchers(HttpMethod.GET,"/api/appointment/fetchAllAppointments").permitAll()
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




}
