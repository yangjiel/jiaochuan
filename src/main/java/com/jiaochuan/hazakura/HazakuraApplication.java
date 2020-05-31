package com.jiaochuan.hazakura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HazakuraApplication extends SpringBootServletInitializer {

	// Used for local runtime
	public static void main(String[] args) {
		SpringApplication.run(HazakuraApplication.class, args);
	}

}
