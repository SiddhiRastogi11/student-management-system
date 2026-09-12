package com.siddhi.studentmanagementsystem.service;

import com.siddhi.studentmanagementsystem.dto.DepartmentResponseDTO;
import com.siddhi.studentmanagementsystem.dto.StudentRequestDTO;
import com.siddhi.studentmanagementsystem.dto.StudentResponseDTO;
import com.siddhi.studentmanagementsystem.entity.Department;
import com.siddhi.studentmanagementsystem.entity.Student;
import com.siddhi.studentmanagementsystem.exception.DuplicateResourceException;
import com.siddhi.studentmanagementsystem.exception.ResourceNotFoundException;
import com.siddhi.studentmanagementsystem.repository.DepartmentRepository;
import com.siddhi.studentmanagementsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {
        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Student with email already exists: " + dto.getEmail());
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .department(department)
                .build();

        Student savedStudent = studentRepository.save(student);
        return mapToResponseDTO(savedStudent);
    }

    public Page<StudentResponseDTO> getAllStudents(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return studentRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return mapToResponseDTO(student);
    }

    public Page<StudentResponseDTO> searchStudents(String name, String deptName, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return studentRepository.searchStudents(name, deptName, pageable).map(this::mapToResponseDTO);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        studentRepository.findByEmail(dto.getEmail()).ifPresent(studentWithEmail -> {
            if (!studentWithEmail.getId().equals(id)) {
                throw new DuplicateResourceException("Email already in use by another student: " + dto.getEmail());
            }
        });

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        existingStudent.setName(dto.getName());
        existingStudent.setEmail(dto.getEmail());
        existingStudent.setAge(dto.getAge());
        existingStudent.setDepartment(department);

        Student updatedStudent = studentRepository.save(existingStudent);
        return mapToResponseDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepository.delete(student);
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {
        DepartmentResponseDTO deptDTO = null;
        if (student.getDepartment() != null) {
            deptDTO = DepartmentResponseDTO.builder()
                    .id(student.getDepartment().getId())
                    .departmentName(student.getDepartment().getDepartmentName())
                    .build();
        }

        return StudentResponseDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .age(student.getAge())
                .department(deptDTO)
                .build();
    }
}