package com.DevLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.DevLibrary")
public class DevLibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevLibraryApplication.class, args);
        
	}
}

