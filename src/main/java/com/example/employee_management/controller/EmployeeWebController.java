package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentStatisticDTO;
import com.example.employee_management.entity.EmployeeEntity;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeWebController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/list")
    public String listEmployees(@RequestParam(required = false) String keyword, Model model) {
        List employees;

        if (keyword != null && !keyword.trim().isEmpty()) {
            employees = employeeRepository.findByNameContainingIgnoreCaseOrDepartmentNameIgnoreCase(keyword, keyword);
        } else {
            employees = employeeRepository.findAll();
        }

        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        return "employees/list"; // Trả về file src/main/resources/templates/employees/list.html
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new EmployeeEntity());
        model.addAttribute("departments", departmentRepository.findAll());
        return "employees/add"; // Trả về file src/main/resources/templates/employees/add.html
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") EmployeeEntity employee,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            return "employees/add";
        }

        employeeRepository.save(employee);
        return "redirect:/employees/list";
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        long totalEmployees = employeeRepository.count();
        List<DepartmentStatisticDTO> departmentStats = employeeRepository.countEmployeesByDepartment();

        model.addAttribute("totalEmployees", totalEmployees);
        model.addAttribute("departmentStats", departmentStats);

        return "employees/statistics"; // Trả về file src/main/resources/templates/employees/statistics.html
    }
}
