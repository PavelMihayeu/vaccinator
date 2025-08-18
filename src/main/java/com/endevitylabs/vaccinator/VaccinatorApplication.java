package com.endevitylabs.vaccinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class, 
    UserDetailsServiceAutoConfiguration.class,
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@Validated
public class VaccinatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaccinatorApplication.class, args);
    }

}
