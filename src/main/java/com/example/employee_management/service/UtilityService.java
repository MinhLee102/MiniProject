package com.example.employee_management.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UtilityService {

    public String generateEmployeeCode() {
        String randomStr = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "NV-" + randomStr;
    }

    public String formatName(String name) {
        if (name == null || name.trim().isEmpty()) return "";
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
