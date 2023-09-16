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
import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import com.Serah.DoctorAppointmentSystem.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;




import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EmailService emailService;


    public String bookAppointment(AppointmentRequest appointmentRequest) {

        try {
            Optional<Appointment> appointmentOptional = appointmentRepository.findAppointmentByDoctorIdAndAppointmentDate(
                    appointmentRequest.getDoctorId(), appointmentRequest.getAppointmentDate());


            if (appointmentOptional.isPresent()) {
                Appointment appointment = appointmentOptional.get();
                Patient patient = appointment.getPatient();
                if (patient != null) {
                    EmailDetails emailDetails = EmailDetails.builder()
                            .recipient(appointmentRequest.getPatientEmail())
                            .subject("Appointment Not Booked")
                            .messageBody("Dear " + patient.getName() + ", Your appointment has not been successfully booked. Kindly choose another date.")
                            .build();

                    return "Appointment Date has already been Booked";
                } else {

                    return "No patient associated with this appointment";
                }
            }
            Long patientId = appointmentRequest.getPatientId();

            Optional<Doctor>optionalDoctor = doctorRepository.findById(appointmentRequest.getDoctorId());
            Optional<Patient>optionalPatient = patientRepository.findById(appointmentRequest.getDoctorId());
            if(optionalPatient.isEmpty() || optionalDoctor.isEmpty())
            {
                return "Patient or doctor does not exist";
            }

            Appointment appointment = Appointment.builder()
                    .appointmentDate(appointmentRequest.getAppointmentDate())
                    .doctor(optionalDoctor.get())
                    .patient(optionalPatient.get())
                    .complain(appointmentRequest.getComplain())
                    .build();
            appointmentRepository.save(appointment);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(appointmentRequest.getPatientEmail())
                    .subject("Appointment Booked Successfully")
                    .messageBody("Dear patient, Your appointment has been successfully booked. Your appointment date is " + appointmentRequest.getAppointmentDate())
                    .build();

            emailService.sendSimpleEmail(emailDetails);
            return "Appointment Successfully Booked";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }


    }
}









