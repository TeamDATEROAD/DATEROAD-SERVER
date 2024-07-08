package org.dateroad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DateroadApplication {

	public static void main(String[] args) {
		SpringApplication.run(DateroadApplication.class, args);
	}

}
