package com.Serah.DoctorAppointmentSystem.appointment.repository;

import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {


    Optional<Appointment> findAppointmentByDoctorIdAndAppointmentDate(Long id, Date date);


}



