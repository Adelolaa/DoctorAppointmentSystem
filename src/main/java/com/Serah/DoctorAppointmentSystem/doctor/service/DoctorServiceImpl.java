package com.Serah.DoctorAppointmentSystem.doctor.service;

import com.Serah.DoctorAppointmentSystem.doctor.dto.DoctorRequest;
import com.Serah.DoctorAppointmentSystem.doctor.dto.GetRequest;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.doctor.repository.DoctorRepository;
import com.Serah.DoctorAppointmentSystem.email.EmailDetails;
import com.Serah.DoctorAppointmentSystem.email.EmailService;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import com.Serah.DoctorAppointmentSystem.response.Data;
import com.Serah.DoctorAppointmentSystem.response.Response;
import com.Serah.DoctorAppointmentSystem.role.Roles;
import com.Serah.DoctorAppointmentSystem.role.RoleRepository;
import com.Serah.DoctorAppointmentSystem.security.JwtTokenProvider;
import com.Serah.DoctorAppointmentSystem.security.dto.LoginRequest;
import com.Serah.DoctorAppointmentSystem.utils.AccountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import static org.apache.catalina.valves.AbstractAccessLogValve.localDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Response createAccount(DoctorRequest doctorRequest) {
        if (doctorRepository.existsByEmail(doctorRequest.getEmail()))
            return Response.builder()
                    .responseCode(AccountUtil.USER_EXISTS_CODE)
                    .responseMessage(AccountUtil.USER_EXISTS_MESSAGE)
                    .data(null)
                    .build();


        Doctor doctor = Doctor.builder()
                .name(doctorRequest.getName())
                .email(doctorRequest.getEmail())
                .speciality(doctorRequest.getSpeciality())
                .hospitalDetails(doctorRequest.getHospitalDetails())
                .password(passwordEncoder.encode(doctorRequest.getPassword()))
                .workingHours(doctorRequest.getWorkingHours())
                .description(doctorRequest.getDescription())
                .gender(doctorRequest.getGender())
                .build();


        Roles roles = roleRepository.findByRoleName("ROLE_DOCTOR").orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        doctor.setRoles(Collections.singleton(roles));

        doctorRepository.save(doctor);
        log.info("Doctor {} is saved", doctor.getName());

        EmailDetails message = EmailDetails.builder()
                .recipient(doctorRequest.getEmail())
                .subject("Doc on the Go")
                .messageBody("Congratulations! your Doc on the Go account has been successfully created " +
                        "Healthcare that goes where you go")
                .build();
        emailService.sendSimpleEmail(message);



        return Response.builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREATION_MESSAGE)
                .data(Data.builder()
                        .name(doctorRequest.getName())
                        .email(doctorRequest.getEmail())
                        .description("Welcome to Doc on the Go" +
                                "\n" + "Healthcare that goes where you go")
                        .build())
                .build();

    }

    @Override
    public ResponseEntity<Response> signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authentication.getName();
        authentication.getCredentials();

        return new  ResponseEntity<>(
                Response.builder()
                        .responseCode(AccountUtil.SUCCESSFUL_LOGIN_RESPONSE_CODE)
                        .responseMessage(AccountUtil.SUCCESSFUL_LOGIN_MESSAGE)
                        .data(Data.builder()
                                .name(authentication.getName())
                                .description("token: " + jwtTokenProvider.generateToken(authentication))
                        .build())
                .build(), HttpStatus.CREATED);


    }

    @Override
    public ResponseEntity<Response> resetPassword(LoginRequest loginRequest) {
        boolean existsByEmail = doctorRepository.existsByEmail(loginRequest.getEmail());

        if (!existsByEmail){
            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.UNSUCCESSFUL_LOGIN_RESPONSE_CODE)
                    .responseMessage(AccountUtil.EMAIL_DOES_NOT_EXIST_MESSAGE)
                    .data(null)
                    .build());
        }else {
            Doctor doctor = doctorRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                    new UsernameNotFoundException("User with this email does not exist"));
            String encoder = passwordEncoder.encode(loginRequest.getPassword());
            doctor.setPassword(encoder);
            doctorRepository.save(doctor);

            return ResponseEntity.ok(Response.builder()
                    .responseCode(AccountUtil.SUCCESS_MESSAGE_CODE)
                    .responseMessage(AccountUtil.SUCCESS_MESSAGE)
                    .data(null)
                    .build());
        }

    }

    @Override
    public Response getDoctorByEmail(GetRequest getRequest) {
        boolean isExistsByEmail= doctorRepository.existsByEmail(getRequest.getEmail());
        if (isExistsByEmail){
            log.info("exist");
            Doctor doctor = doctorRepository.findByEmail(getRequest.getEmail()).get();

            return Response.builder()
                    .responseCode(AccountUtil.SUCCESS_MESSAGE_CODE)
                    .responseMessage(AccountUtil.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .name(doctor.getName())
                            .email(doctor.getEmail())
                            .speciality(doctor.getSpeciality())
                            .build())
                    .build();



        }else{
            log.info("not exist");
            return Response.builder()
                    .responseCode(AccountUtil.USER_NOT_FOUND_CODE)
                    .responseMessage(AccountUtil.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
    }

    @Override
    public List<Response> getAllDoctor() {
        List<Doctor> doctorList = doctorRepository.findAll();

        List<Response> responses = new ArrayList<>();

        for (Doctor doctors: doctorList){
            responses.add(Response.builder()
                    .responseCode(AccountUtil.SUCCESS_MESSAGE_CODE)
                    .responseMessage(AccountUtil.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .name(doctors.getName())
                            .speciality(doctors.getSpeciality())
                            .email(doctors.getEmail())
                            .description(doctors.getRoles().toString())
                            .build())
                    .build());
        }
        return responses;
    }

    @Override
    public Doctor randomDoctorOnSpecificDay(LocalDate localDate) {
        List<Doctor> doctors = new ArrayList<>(doctorRepository.findAll().stream()
                .filter(doctor -> doctor.getAvailableDays()
                        .contains(localDate.getDayOfWeek().toString())).toList());
        Collections.shuffle(doctors);
        return doctors.iterator().next();

    }


    @Override
    public Doctor randomizingRandomDoctors(Doctor previousDoctor, LocalDate localDate) {
        List<Doctor> doctors = doctorRepository.findAll().stream().filter(doctor ->
                doctor.getAvailableDays().contains(localDate.getDayOfWeek().toString())).toList();
        List<Doctor> newDoctors = new ArrayList<>(doctors.stream().filter(doctor ->
                !doctor.equals(previousDoctor)).toList());
        Collections.shuffle(newDoctors);
        return doctors.iterator().next();
    }

    @Override
    public String deleteDoctor(long id) {
            Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            doctor.setAvailable(false);
            doctorRepository.save(doctor);
            return "Doctor  deleted successfully";
    }

}


