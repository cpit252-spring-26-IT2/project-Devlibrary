package com.DevLibrary.DevLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.DevLibrary")
public class DevLibraryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DevLibraryApplication.class, args);
/*
example for the using of builder:
BookResource book = new BookResource.Builder(
        "Clean Code",
        "CPIT-252",
        "https://example.com/book"
)
.description("Good programming book")
.author("Robert Martin")
.build();

without author is:
BookResource book = new BookResource.Builder(
        "Java Basics",
        "CPIT-252",
        "https://example.com/java"
).build();



 */
	}
}

