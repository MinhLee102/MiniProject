package com.example.employee_management.controller;

import com.example.employee_management.entity.EmployeeEntity;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> searchEmployees(
            @RequestParam(required = false) String keyword) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(
                    employeeRepository.findByNameContainingIgnoreCaseOrDepartmentNameIgnoreCase(keyword, keyword)
            );
        }
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    // 2. Thêm mới nhân viên vào DB
    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(@RequestBody EmployeeEntity employee) {
        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // 3. Cập nhật thông tin nhân viên
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeEntity> updateEmployee(@PathVariable Long id, @RequestBody EmployeeEntity employeeDetails) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setName(employeeDetails.getName());
            emp.setEmail(employeeDetails.getEmail());
            emp.setDepartment(employeeDetails.getDepartment());
            return ResponseEntity.ok(employeeRepository.save(emp));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Xóa nhân viên theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return ResponseEntity.ok("Xóa nhân viên thành công!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy ID nhân viên để xóa!");
    }
}

/**
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final List<Employee> employeeList = new ArrayList<>();
    private Long currentId = 1L;

    public EmployeeController() {
        // Dữ liệu mẫu ban đầu
        employeeList.add(new Employee(currentId++, "Nguyen Van A", "a.nguyen@email.com", "IT"));
        employeeList.add(new Employee(currentId++, "Tran Thi B", "b.tran@email.com", "HR"));
    }

    // List employees API
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeList);
    }

    // employee detail API
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return employeeList.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy nhân viên!"));
    }

    // add new employee API
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        employee.setId(currentId++);
        employeeList.add(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }
}
**/