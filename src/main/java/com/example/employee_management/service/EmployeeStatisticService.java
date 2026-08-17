package com.example.employee_management.service;

import com.example.employee_management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmployeeStatisticService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeStatisticService.class);
    private final EmployeeRepository employeeRepository;

    public EmployeeStatisticService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable(value = "employeeCount")
    public long getTotalEmployees() {
        log.info("==> ĐANG QUERY TỪ DATABASE ĐỂ ĐẾM SỐ LƯỢNG NHÂN VIÊN...");
        return employeeRepository.count();
    }

    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = "employeeCount", allEntries = true)
    public void clearEmployeeCountCache() {
        log.info("[CACHE EVICT] Đã xóa cache 'employeeCount' (Hết hạn 1 phút)");
    }
}