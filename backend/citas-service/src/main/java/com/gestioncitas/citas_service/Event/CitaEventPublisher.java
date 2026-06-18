package com.gestioncitas.citas_service.Event;

import com.gestioncitas.citas_service.Config.RabbitMQConfig;
import com.gestioncitas.citas_service.dto.CitaEventoDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CitaEventPublisher {

    public static final String ROUTING_RESERVADA = "cita.reservada";
    public static final String ROUTING_ATENDIDA = "cita.atendida";
    public static final String ROUTING_CANCELADA = "cita.cancelada";

    private final RabbitTemplate rabbitTemplate;

    public CitaEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar(String routingKey, CitaEventoDTO evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_CITAS, routingKey, evento);
    }
}
