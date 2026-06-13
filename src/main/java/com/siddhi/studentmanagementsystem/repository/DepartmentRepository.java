package com.siddhi.studentmanagementsystem.repository;

import com.siddhi.studentmanagementsystem.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
