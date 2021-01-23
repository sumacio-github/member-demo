package io.sumac.demo.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("io.sumac.demo.member")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
