package com.siddhi.studentmanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequestDTO {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters")
    private String departmentName;
}