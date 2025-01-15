package com.optimal.virtualemp;

import com.optimal.virtualemp.resource.UserController;
import com.optimal.virtualemp.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses= {UserController.class, UserService.class})
public class VirtualEmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualEmployeeApplication.class, args);
	}

}
