package com.example.employee_management.controller;

import com.example.employee_management.service.UtilityService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    private final UtilityService utilityService;
    private final ModelMapper modelMapper;

    public EmployeeController(UtilityService utilityService, ModelMapper modelMapper) {
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/generate-employee-code")
    public String generateEmployeeCode(@RequestParam(defaultValue = "le duc minh") String name) {
        String formattedName = utilityService.formatName(name);
        String empCode = utilityService.generateEmployeeCode();

        return "Tên: " + formattedName + " | Mã NV: " + empCode;
    }

    @GetMapping("/test-mapper")
    public String testMapper() {
        boolean isMapperNotNull = (modelMapper != null);
        return "ModelMapper đã được inject thành công: " + isMapperNotNull;
    }
}
