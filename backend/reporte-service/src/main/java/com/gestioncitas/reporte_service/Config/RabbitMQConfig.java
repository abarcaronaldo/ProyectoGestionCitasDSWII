package com.gestioncitas.reporte_service.Config;

import com.gestioncitas.reporte_service.dto.EventoCitaDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    /** Mismo exchange que declaro citas-service (3.9): el productor y el consumidor solo se conocen por su nombre. */
    public static final String EXCHANGE_CITAS = "citas.eventos";
    public static final String QUEUE_EVENTOS_CITA = "reporte.citas.eventos";
    public static final String ROUTING_PATTERN_CITA = "cita.*";

    @Bean
    public TopicExchange citasExchange() {
        return new TopicExchange(EXCHANGE_CITAS);
    }

    @Bean
    public Queue eventosCitaQueue() {
        return new Queue(QUEUE_EVENTOS_CITA, true);
    }

    @Bean
    public Binding eventosCitaBinding(Queue eventosCitaQueue, TopicExchange citasExchange) {
        return BindingBuilder.bind(eventosCitaQueue).to(citasExchange).with(ROUTING_PATTERN_CITA);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        // citas-service publica con su propio DTO (com.gestioncitas.citas_service.dto.CitaEventoDTO);
        // su Jackson2JsonMessageConverter pone ese nombre en la cabecera __TypeId__. Aqui lo mapeamos
        // al DTO equivalente de este servicio: cada microservicio define su propio contrato de lectura.
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(Map.of(
                "com.gestioncitas.citas_service.dto.CitaEventoDTO", EventoCitaDTO.class));
        classMapper.setTrustedPackages("*");

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper);
        return converter;
    }
}
