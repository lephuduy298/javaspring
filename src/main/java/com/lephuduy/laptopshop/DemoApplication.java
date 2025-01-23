package com.lephuduy.laptopshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext abc = SpringApplication.run(DemoApplication.class, args);
		for (String s : abc.getBeanDefinitionNames()) {
			System.out.println(s);
		}
	}
}
