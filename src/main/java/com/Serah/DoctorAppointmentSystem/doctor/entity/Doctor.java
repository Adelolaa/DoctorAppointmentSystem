package com.Serah.DoctorAppointmentSystem.doctor.entity;

import com.Serah.DoctorAppointmentSystem.appointment.entity.Appointment;
import com.Serah.DoctorAppointmentSystem.role.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String speciality;
    private String email;
    private String hospitalDetails;
    private String workingHours;
    private String password;
    private String gender;
    private String description;
    private boolean isAvailable;
    private String availableDays;
    private String uniqueIdentifier;
    @CreationTimestamp
    private LocalDate dateCreated;
    @UpdateTimestamp
    private LocalDate dateUpdated;
// @ElementCollection
// private List<Long> acceptedAppointments = new ArrayList<>();
//
 @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 @JoinTable(name = "doctor_roles", joinColumns = @JoinColumn(name = "doctor_id", referencedColumnName = "id"),
         inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
 private Set<Roles> roles;

    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;


}
