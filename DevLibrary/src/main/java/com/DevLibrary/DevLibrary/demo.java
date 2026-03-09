package com.DevLibrary.DevLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class demo {
	public static void main(String[] args) {
		Resource resource1 = new BookResource(
				"B1",
				"Java Basics",
				"CPIT-252",
				"A helpful Java book for beginners",
				"https://example.com/java-book",
				"Azzam",
				"John Smith"
		);

		Resource resource2 = new SlidesResource(
				"S1",
				"Week 1 Slides",
				"CPIT-252",
				"Introduction slides",
				"https://example.com/slides1",
				"Hossam",
				1
		);

		System.out.println(resource1);
		System.out.println(resource2);
	}
}

