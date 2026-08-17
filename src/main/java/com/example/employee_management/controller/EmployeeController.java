package com.example.employee_management.controller;

import com.example.employee_management.entity.EmployeeEntity;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> searchEmployees(
            @RequestParam(required = false) String keyword) {
        log.info("Nhận yêu cầu tìm kiếm nhân viên với keyword: '{}'", keyword);
        if (keyword != null && !keyword.trim().isEmpty()) {
            return ResponseEntity.ok(
                    employeeRepository.findByNameContainingIgnoreCaseOrDepartmentNameIgnoreCase(keyword, keyword)
            );
        }
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getEmployeeById(@PathVariable Long id) {
        EmployeeEntity employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeEntity> createEmployee(@Valid @RequestBody EmployeeEntity employee) {
        log.info("Bắt đầu tạo nhân viên mới: email={}", employee.getEmail());
        EmployeeEntity savedEmployee = employeeRepository.save(employee);
        log.info("Tạo nhân viên thành công với ID: {}", savedEmployee.getId());
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // 3. Cập nhật thông tin nhân viên
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeEntity> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeEntity employeeDetails) {
        log.info("Bắt đầu cập nhật thông tin nhân viên ID: {}", id);
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
        log.info("Nhận yêu cầu xóa nhân viên ID: {}", id);
        if (!employeeRepository.existsById(id)) {
            log.error("Xóa thất bại: Nhân viên ID {} không tồn tại", id);
            throw new ResourceNotFoundException("Không thể xóa. Không tìm thấy nhân viên với ID: " + id);
        }
        employeeRepository.deleteById(id);
        log.info("Xóa thành công nhân viên ID: {}", id);
        return ResponseEntity.ok("Xóa nhân viên thành công!");
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