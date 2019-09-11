package main;

import main.controllers.UserController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class Izanagi {
    public static void main(String[] args) {
        SpringApplication.run(Izanagi.class, args);
    }
}