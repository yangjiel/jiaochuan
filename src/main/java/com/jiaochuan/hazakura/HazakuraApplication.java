package com.jiaochuan.hazakura;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HazakuraApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(HazakuraApplication.class);

	// Used for local runtime
	public static void main(String[] args) {
		log.warn("Test!");
		SpringApplication.run(HazakuraApplication.class, args);
	}
}
