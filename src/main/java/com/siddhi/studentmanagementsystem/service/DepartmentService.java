package com.siddhi.studentmanagementsystem.service;

import com.siddhi.studentmanagementsystem.dto.DepartmentRequestDTO;
import com.siddhi.studentmanagementsystem.dto.DepartmentResponseDTO;
import com.siddhi.studentmanagementsystem.entity.Department;
import com.siddhi.studentmanagementsystem.exception.DuplicateResourceException;
import com.siddhi.studentmanagementsystem.exception.ResourceNotFoundException;
import com.siddhi.studentmanagementsystem.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public DepartmentResponseDTO saveDepartment(DepartmentRequestDTO requestDTO) {
        if (departmentRepository.existsByDepartmentName(requestDTO.getDepartmentName())) {
            throw new DuplicateResourceException("Department with name '" + requestDTO.getDepartmentName() + "' already exists");
        }

        Department department = Department.builder()
                .departmentName(requestDTO.getDepartmentName())
                .build();

        Department savedDepartment = departmentRepository.save(department);
        return mapToResponseDTO(savedDepartment);
    }

    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DepartmentResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponseDTO(department);
    }

    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!existingDepartment.getDepartmentName().equalsIgnoreCase(requestDTO.getDepartmentName()) &&
                departmentRepository.existsByDepartmentName(requestDTO.getDepartmentName())) {
            throw new DuplicateResourceException("Department with name '" + requestDTO.getDepartmentName() + "' already exists");
        }

        existingDepartment.setDepartmentName(requestDTO.getDepartmentName());
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return mapToResponseDTO(updatedDepartment);
    }

    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponseDTO mapToResponseDTO(Department department) {
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .departmentName(department.getDepartmentName())
                .build();
    }
}