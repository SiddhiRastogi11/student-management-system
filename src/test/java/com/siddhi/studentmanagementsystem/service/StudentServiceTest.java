package com.siddhi.studentmanagementsystem.service;

import com.siddhi.studentmanagementsystem.dto.StudentRequestDTO;
import com.siddhi.studentmanagementsystem.dto.StudentResponseDTO;
import com.siddhi.studentmanagementsystem.entity.Department;
import com.siddhi.studentmanagementsystem.entity.Student;
import com.siddhi.studentmanagementsystem.exception.DuplicateResourceException;
import com.siddhi.studentmanagementsystem.exception.ResourceNotFoundException;
import com.siddhi.studentmanagementsystem.repository.DepartmentRepository;
import com.siddhi.studentmanagementsystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private StudentService studentService;

    private Department department;
    private Student student;
    private StudentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .departmentName("Computer Science")
                .build();

        student = Student.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .age(21)
                .department(department)
                .build();

        requestDTO = StudentRequestDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .age(21)
                .departmentId(1L)
                .build();
    }

    @Test
    @DisplayName("Should create student successfully")
    void createStudent_Success() {
        when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
        when(departmentRepository.findById(requestDTO.getDepartmentId())).thenReturn(Optional.of(department));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponseDTO result = studentService.createStudent(requestDTO);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void createStudent_DuplicateEmail_ThrowsException() {
        when(studentRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(student));

        assertThrows(DuplicateResourceException.class, () -> studentService.createStudent(requestDTO));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student ID not found")
    void getStudentById_NotFound_ThrowsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.getStudentById(99L));
    }
}