package org.dateroad;

import org.dateroad.auth.jwt.JwtGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan
@SpringBootApplication
public class DateroadApplication {

	public static void main(String[] args) {
		SpringApplication.run(DateroadApplication.class, args);
	}

}
