package com.example.employee_management;

import com.example.employee_management.controller.EmployeeController;
import com.example.employee_management.controller.HelloController;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class EmployeeManagementApplication {

	public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(EmployeeManagementApplication.class, args);
	}

        HelloController helloController() {
            return new HelloController();
        }

}
