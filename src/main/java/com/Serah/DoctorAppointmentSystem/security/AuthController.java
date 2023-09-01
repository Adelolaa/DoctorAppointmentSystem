package com.Serah.DoctorAppointmentSystem.security;

import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.doctor.repository.DoctorRepository;
import com.Serah.DoctorAppointmentSystem.doctor.dto.DoctorRequest;
import com.Serah.DoctorAppointmentSystem.response.AuthResponse;
import com.Serah.DoctorAppointmentSystem.role.RoleRepository;
import com.Serah.DoctorAppointmentSystem.role.Roles;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import com.Serah.DoctorAppointmentSystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private RoleRepository roleRepository;
//    Logger logger = (Logger) LoggerFactory.getLogger(Logger.class);
    @Autowired
    PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody DoctorRequest doctorRequest){
        if (doctorRepository.existsByName(doctorRequest.getName())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        Doctor doctor =Doctor.builder()
                .name(doctorRequest.getName())
                .email(doctorRequest.getEmail())
                .speciality(doctorRequest.getSpeciality())
                .description(doctorRequest.getDescription())
                .hospitalDetails(doctorRequest.getHospitalDetails())
                .password(passwordEncoder.encode(doctorRequest.getPassword()))
                .build();
        Roles role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        doctor.setRoles(Collections.singleton(role));

        doctorRepository.save(doctor);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }






}
