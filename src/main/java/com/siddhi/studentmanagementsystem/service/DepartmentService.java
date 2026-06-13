package com.siddhi.studentmanagementsystem.service;

import com.siddhi.studentmanagementsystem.entity.Department;
import com.siddhi.studentmanagementsystem.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department saveDepartment(Department department) {
        return  departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return  departmentRepository.findAll();
    }
}
