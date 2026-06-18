package com.gestioncitas.citas_service.Client;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

// Sin @Configuration: se referencia explicitamente via @FeignClient(configuration = ...)
// para que aplique solo al cliente que la declara, no a todo el contexto de la app.
public class FeignRetryConfig {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 3);
    }
}
