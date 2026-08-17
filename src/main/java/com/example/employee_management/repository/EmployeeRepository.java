package com.example.employee_management.repository;

import com.example.employee_management.dto.DepartmentStatisticDTO;
import com.example.employee_management.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List findByNameContainingIgnoreCase(String name);

    List findByDepartmentNameIgnoreCase(String departmentName);

    List findByNameContainingIgnoreCaseOrDepartmentNameIgnoreCase(String name, String departmentName);

    // JPQL gom nhóm đếm số lượng nhân viên theo từng phòng ban
    @Query("SELECT new com.example.employee_management.dto.DepartmentStatisticDTO(" +
            "COALESCE(d.name, 'Chưa phân công'), COUNT(e)) " +
            "FROM EmployeeEntity e LEFT JOIN e.department d " +
            "GROUP BY d.name")
    List<DepartmentStatisticDTO> countEmployeesByDepartment();
}

