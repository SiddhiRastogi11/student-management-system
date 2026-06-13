package com.siddhi.studentmanagementsystem.service;

import com.siddhi.studentmanagementsystem.entity.Student;
import com.siddhi.studentmanagementsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor injected
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Delete student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Update student
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(()-> new RuntimeException("Student not found"));
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setAge(updatedStudent.getAge());

        return  studentRepository.save(existingStudent);
    }

    public org.springframework.data.domain.Page<Student> getStudentsPaginated(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return  studentRepository.findAll(pageable);
    }
}

