package com.siddhi.studentmanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "students") // Explicit table name improves readability and avoids default naming issues
// Lombok reduces boilerplate code by auto-generating accessor methods
@Getter
@Setter
@NoArgsConstructor // Required by JPA for entity instantiation
@AllArgsConstructor // Useful for object creation with all fields
@Builder // Enables builder pattern for flexible object construction
public class Student {
    @Id // primary key for student table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Enables auto-increment primary key generation in MySQL

    private Long id;

    // Student name cannot be null
    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private  String name;

    // Email must be unique for each student
    @NotBlank(message = "Email cannot be empty")
    @Email(message =  "Please provide a valid email address")
    @Column(unique = true,nullable = false)
    private  String email;

    // Age is optional but useful for filtering later

    @Min(value = 16, message = "Student must be at least 16 years old")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "department_id") // This creates the Foreign Key in the database
    private Department department;

}
