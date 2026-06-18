package com.gestioncitas.historial_medico_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HistorialMedicoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistorialMedicoServiceApplication.class, args);
	}

}
