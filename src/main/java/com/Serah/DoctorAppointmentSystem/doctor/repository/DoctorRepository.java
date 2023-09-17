package com.Serah.DoctorAppointmentSystem.doctor.repository;


import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import com.Serah.DoctorAppointmentSystem.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Boolean existsByEmail (String Email);
    Optional<Doctor> findByEmail(String Email);

    Boolean existsByName(String Name);


    List<Doctor> findBySpeciality(String speciality);


}


