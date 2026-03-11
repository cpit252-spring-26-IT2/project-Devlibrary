package com.DevLibrary.DevLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class demo {
	public static void main(String[] args) {


        //here we used the factory method (ResourceFactory) and made resource1 then set the values
        //using set method that add the value to the factory method object we made.
        Resource resource1 =ResorceFactory.generateResource("book");
        resource1.setId("B1");
        resource1.setTitle("java basics");
        resource1.setCourseName("CPIT-252");
        resource1.setDescription("A helpful Java book for beginners");
        resource1.setLink("https://example.com/java-book");
        resource1.setUploadedBy("Azzam");
        ((BookResource)resource1).setAuthor("John Smith");

        //same as the one before

        Resource resource2 =ResorceFactory.generateResource("slide");
        resource2.setId("S1");
        resource2.setTitle("Week 1 Slides");
        resource2.setCourseName("CPIT-252");
        resource2.setDescription("Introduction slides");
        resource2.setLink("https://example.com/slides1");
        resource2.setUploadedBy("Hossam");
        ((SlidesResource)resource2).setWeekNumber(1);



		System.out.println(resource1);
		System.out.println(resource2);
	}
}

