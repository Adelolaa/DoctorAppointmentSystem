package com.Serah.DoctorAppointmentSystem.appointment.service;

import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentRequest;
import com.Serah.DoctorAppointmentSystem.appointment.dto.AppointmentResponse;
import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import com.Serah.DoctorAppointmentSystem.appointment.repository.AppointmentRepository;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import com.Serah.DoctorAppointmentSystem.doctor.repository.DoctorRepository;
import com.Serah.DoctorAppointmentSystem.doctor.service.DoctorService;
import com.Serah.DoctorAppointmentSystem.email.EmailDetails;
import com.Serah.DoctorAppointmentSystem.email.EmailService;
import com.Serah.DoctorAppointmentSystem.patient.dto.PatientRequest;
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import com.Serah.DoctorAppointmentSystem.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;

    private final PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private DoctorService doctorService;
    private EmailService emailService;
    private ModelMapper modelMapper;


    @Override
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {

        Patient patient = patientRepository.findUserByUsername(appointmentRequest.getUsername()).orElseThrow(() -> new
                ResponseStatusException(HttpStatus.NOT_FOUND));

        Appointment appointment = Appointment.builder()
                .appointmentDate(String.valueOf(appointmentRequest.getLocalDate()))
                .patient((Set<Patient>) patient)
                .comments(appointmentRequest.getMessage())
                .remarks("Appointment successfully booked")
                .isFulfilled(false)
                .doctor(doctorService.randomDoctorOnSpecificDay(appointmentRequest.getLocalDate()))
                .build();

        appointmentRepository.save(appointment);
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(patient.getEmail())
                .subject("Appointment Booked Successfully")
                .messageBody("Hi " + patient.getUsername() + ", Our Doctors are here to care for you 24/7.\n" +
                        "\n" +
                        "           Our amiable  Dr. " + doctor.getName()  + " will be at your service on your preferred date " + appointmentRequest.getLocalDate() + "\n" +
                        "           Doc on the Go, Healthcare that goes where you go.")
                .build();

        emailService.sendSimpleEmail(emailDetails);



        return AppointmentResponse.builder()
                .username(appointment.getPatient().stream().map(username -> patient.getUsername()).toString())
                .name(doctor.getName())
                .response(appointment.getRemarks())
                .build();
    }

    @Override
    public AppointmentResponse updateAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Doctor doctor = appointment.getDoctor();
        appointment.setDoctor(doctorService.randomizingRandomDoctors(doctor, LocalDate.parse(appointment.getAppointmentDate())));
        appointmentRepository.save(appointment);
        return modelMapper.map(appointment, AppointmentResponse.class);
    }

    @Override
    public List<AppointmentResponse> viewAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentEntity -> AppointmentResponse.builder()
                .username(appointmentEntity.getPatient().getClass().getName())
                .name(appointmentEntity.getDoctor().getName())
                .response(appointmentEntity.getRemarks())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> findAppointmentByUser(String username) {
        List<Appointment> list = appointmentRepository.findByPatient_Email(username);
        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();

        for (Appointment element :list){
            appointmentResponseList.add(AppointmentResponse.builder()
                            .username(element.getPatient().getClass().getName())
                            .name(element.getDoctor().getName())
                            .response(element.getRemarks())
                            .build());
        }
        return appointmentResponseList;

     }

    @Override
    public AppointmentResponse viewSingleAppointment(Long id) {
        return appointmentRepository.findById(id).map(appointmentEntity -> AppointmentResponse.builder()
                .username(appointmentEntity.getPatient().getClass().getName())
                .name(appointmentEntity.getDoctor().getName())
                .response(appointmentEntity.getRemarks())
                .build()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public String markAppointmentAsFulfilled(Long id) {
        Appointment appointmentEntity = appointmentRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        appointmentEntity.setFulfilled(true);
        appointmentRepository.save(appointmentEntity);
        return "Appointment fulfilled";
    }

    @Override
    public List<AppointmentResponse> fetchFulfilledAppointments() {
        List<Appointment> appointmentEntities = appointmentRepository.findAll();
        return appointmentEntities.stream().filter(Appointment::isFulfilled)
                                    .map(appointmentEntity -> AppointmentResponse.builder()
                .username(appointmentEntity.getPatient().getClass().getName())
                .name(appointmentEntity.getDoctor().getName())
                .response(appointmentEntity.getRemarks())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> fetchUnfulfilledAppointments() {
        List<Appointment> appointmentEntities = appointmentRepository.findAll();
        return appointmentEntities.stream().filter(appointment ->
                !appointment.isFulfilled()).map(appointmentEntity -> AppointmentResponse.builder()
                .username(appointmentEntity.getPatient().getClass().getName())
                .name(appointmentEntity.getDoctor().getName())
                .response(appointmentEntity.getRemarks())
                .build()).collect(Collectors.toList());
    }



}


