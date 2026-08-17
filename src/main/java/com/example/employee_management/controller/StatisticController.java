package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentStatisticDTO;
import com.example.employee_management.repository.EmployeeRepository;
import com.example.employee_management.service.EmployeeStatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/statistics")
public class StatisticController {

    private final EmployeeStatisticService statisticService;
    private final EmployeeRepository employeeRepository;

    public StatisticController(EmployeeStatisticService statisticService, EmployeeRepository employeeRepository) {
        this.statisticService = statisticService;
        this.employeeRepository = employeeRepository;
    }

    // 1. Thống kê tổng số nhân viên (có cache)
    @GetMapping("/total-employees")
    public ResponseEntity<Map<String, Object>> getTotalEmployees() {
        long total = statisticService.getTotalEmployees();
        Map<String, Object> response = new HashMap<>();
        response.put("totalEmployees", total);
        return ResponseEntity.ok(response);
    }

    // 2. Thống kê số lượng nhân viên theo từng phòng ban
    @GetMapping("/by-department")
    public ResponseEntity<List<DepartmentStatisticDTO>> getDepartmentStatistics() {
        return ResponseEntity.ok(employeeRepository.countEmployeesByDepartment());
    }
}