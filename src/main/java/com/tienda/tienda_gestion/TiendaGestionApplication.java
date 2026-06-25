package com.tienda.tienda_gestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TiendaGestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaGestionApplication.class, args);
	}

}
