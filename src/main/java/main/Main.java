package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext("main");

        SpringApplication.run(Main.class, args);

    }

    public static void startMainOld(){

        long start = System.currentTimeMillis();


        long finish = System.currentTimeMillis();
        long elapsed = (finish - start) / 1000;
        System.out.println("Прошло времени, сек: " + elapsed);
    }
}
