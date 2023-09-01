package com.Serah.DoctorAppointmentSystem.service;

import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.doctor.repository.DoctorRepository;
//import com.Serah.DoctorAppointmentSystem.patient.Doctor;
//import com.Serah.DoctorAppointmentSystem.role.DoctorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DoctorRepository doctorRepository;

    public CustomUserDetailsService(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Doctor user = doctorRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User with provided Credentials not found "+ email));

        //retrieve roles associated with the user
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());


        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),authorities);
    }


}
