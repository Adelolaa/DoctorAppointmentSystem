package com.Serah.DoctorAppointmentSystem.service;

import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import com.Serah.DoctorAppointmentSystem.appointment.repository.AppointmentRepository;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.doctor.repository.DoctorRepository;
//import com.Serah.DoctorAppointmentSystem.patient.Doctor;
//import com.Serah.DoctorAppointmentSystem.role.DoctorRepository;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import com.Serah.DoctorAppointmentSystem.patient.repository.PatientRepository;
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
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;


    public CustomUserDetailsService(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository){
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (doctorRepository.existsByEmail(email)) {
            Doctor user = doctorRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User with provided Credentials not found " + email));

            Set<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(), authorities);
        }else if(patientRepository.existsByEmail(email)){
            Patient patient = patientRepository.findByEmail(email) .orElseThrow(() -> new UsernameNotFoundException("User with provided Credentials not found " + email));;
            Set<GrantedAuthority> authorities = patient.getRole().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

            return new org.springframework.security.core.userdetails.User(patient.getEmail(),
                    patient.getPassword(), authorities);
        }

        else {

            throw new UsernameNotFoundException("User not found with username: " + email);

        }

    }

}
