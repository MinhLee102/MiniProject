package com.example.employee_management.repository;

import com.example.employee_management.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List findByNameContainingIgnoreCase(String name);

    List findByDepartmentNameIgnoreCase(String departmentName);

    List findByNameContainingIgnoreCaseOrDepartmentNameIgnoreCase(String name, String departmentName);
}
