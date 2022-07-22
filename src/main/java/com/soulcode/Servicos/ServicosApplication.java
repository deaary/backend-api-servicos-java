package com.soulcode.Servicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableCaching  // comentar caso o redis nao esteja ok ainda
@SpringBootApplication
public class ServicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicosApplication.class, args);}




}
