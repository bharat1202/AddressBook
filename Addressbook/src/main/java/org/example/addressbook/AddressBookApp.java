package org.example.addressbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching  // Enable caching support
public class AddressBookApp {
    public static void main(String[] args) {
        SpringApplication.run(AddressBookApp.class, args);
    }
}
