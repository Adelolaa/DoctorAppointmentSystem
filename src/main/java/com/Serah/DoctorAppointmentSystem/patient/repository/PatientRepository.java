package com.Serah.DoctorAppointmentSystem.patient.repository;

import com.Serah.DoctorAppointmentSystem.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Patient> findUserByUsername(String username);

}
