package com.siddhi.studentmanagementsystem.controller;

import com.siddhi.studentmanagementsystem.dto.StudentRequestDTO;
import com.siddhi.studentmanagementsystem.dto.StudentResponseDTO;
import com.siddhi.studentmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO createdStudent = studentService.createStudent(dto);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<StudentResponseDTO> students = studentService.getAllStudents(page, size, sortBy, direction);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponseDTO>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String deptName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<StudentResponseDTO> results = studentService.searchStudents(name, deptName, page, size, sortBy, direction);
        return ResponseEntity.ok(results);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO dto) {
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}