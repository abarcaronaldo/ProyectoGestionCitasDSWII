package com.gestioncitas.reporte_service.Event;

import com.gestioncitas.reporte_service.Config.RabbitMQConfig;
import com.gestioncitas.reporte_service.Entity.MetricaCita;
import com.gestioncitas.reporte_service.Repository.MetricaCitaRepository;
import com.gestioncitas.reporte_service.dto.EventoCitaDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CitaEventoListener {

    private final MetricaCitaRepository metricaCitaRepository;

    public CitaEventoListener(MetricaCitaRepository metricaCitaRepository) {
        this.metricaCitaRepository = metricaCitaRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_EVENTOS_CITA)
    public void recibir(EventoCitaDTO evento) {
        MetricaCita metrica = new MetricaCita();
        metrica.setCitaId(evento.citaId());
        metrica.setPacienteId(evento.pacienteId());
        metrica.setMedicoId(evento.medicoId());
        metrica.setEspecialidadId(evento.especialidadId());
        metrica.setFechaHora(evento.fechaHora());
        metrica.setEstado(evento.estado());
        metrica.setOcurridoEn(evento.ocurridoEn());
        metricaCitaRepository.save(metrica);
    }
}
