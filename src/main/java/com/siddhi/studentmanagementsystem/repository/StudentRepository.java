package com.siddhi.studentmanagementsystem.repository;

import com.siddhi.studentmanagementsystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Student> findByDepartmentId(Long departmentId, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE " +
            "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:deptName IS NULL OR LOWER(s.department.departmentName) LIKE LOWER(CONCAT('%', :deptName, '%')))")
    Page<Student> searchStudents(@Param("name") String name,
                                 @Param("deptName") String deptName,
                                 Pageable pageable);
}