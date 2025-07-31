package com.endevitylabs.vaccinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication
@Validated
@EnableCaching
public class VaccinatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaccinatorApplication.class, args);
	}

}
