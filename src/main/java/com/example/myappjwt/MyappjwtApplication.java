package com.example.myappjwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.myappjwt.mapper")
public class MyappjwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyappjwtApplication.class, args);
	}
}