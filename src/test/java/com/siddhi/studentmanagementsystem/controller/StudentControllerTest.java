package com.siddhi.studentmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siddhi.studentmanagementsystem.dto.DepartmentResponseDTO;
import com.siddhi.studentmanagementsystem.dto.StudentRequestDTO;
import com.siddhi.studentmanagementsystem.dto.StudentResponseDTO;
import com.siddhi.studentmanagementsystem.exception.GlobalExceptionHandler;
import com.siddhi.studentmanagementsystem.exception.ResourceNotFoundException;
import com.siddhi.studentmanagementsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /students - Should return 201 Created on valid payload")
    void createStudent_ValidPayload_Returns201() throws Exception {
        StudentRequestDTO request = StudentRequestDTO.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .age(20)
                .departmentId(1L)
                .build();

        DepartmentResponseDTO deptResponse = DepartmentResponseDTO.builder()
                .id(1L)
                .departmentName("Computer Science")
                .build();

        StudentResponseDTO response = StudentResponseDTO.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .age(20)
                .department(deptResponse)
                .build();

        when(studentService.createStudent(any(StudentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    @DisplayName("POST /students - Should return 400 Bad Request when validation fails")
    void createStudent_InvalidPayload_Returns400() throws Exception {
        StudentRequestDTO invalidRequest = StudentRequestDTO.builder()
                .name("")
                .email("not-an-email")
                .age(12)
                .departmentId(null)
                .build();

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    @DisplayName("GET /students/{id} - Should return 404 when student not found")
    void getStudentById_NotFound_Returns404() throws Exception {
        when(studentService.getStudentById(99L))
                .thenThrow(new ResourceNotFoundException("Student not found with id: 99"));

        mockMvc.perform(get("/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Student not found with id: 99"));
    }
}