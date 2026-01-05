//package - folder with rules, java uses package as namespace. Spring uses pockage for component scanning.
//that why component, service, repo should be in com.project name.
package com.smartparking.smart_parking_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartParkingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartParkingBackendApplication.class, args);
	}

}
