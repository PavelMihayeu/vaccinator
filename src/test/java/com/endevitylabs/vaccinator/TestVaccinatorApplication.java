package com.endevitylabs.vaccinator;

import org.springframework.boot.SpringApplication;

public class TestVaccinatorApplication {

	public static void main(String[] args) {
		SpringApplication.from(VaccinatorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
