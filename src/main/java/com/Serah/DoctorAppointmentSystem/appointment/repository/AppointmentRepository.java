package com.Serah.DoctorAppointmentSystem.appointment.repository;

import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

//    boolean existsByEmail (String email);
//
//    boolean existsById(Long id);
//
//    Optional<Appointment> findById(Long id);

    List<Appointment> findByPatient_Email(String username);

   // boolean existsByAppointmentDate(String appointmentDate);
}
