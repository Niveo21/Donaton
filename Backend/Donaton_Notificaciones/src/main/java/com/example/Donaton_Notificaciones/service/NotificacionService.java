package com.example.Donaton_Notificaciones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.Donaton_Notificaciones.model.Notificacion;
import com.example.Donaton_Notificaciones.repository.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    public void almacenarNotificacion(Notificacion notificacion) {
        if (notificacion.getFechaEnvio() == null) {
            notificacion.setFechaEnvio(java.time.LocalDateTime.now());
        }
        Notificacion guardada = notificacionRepository.save(notificacion);
        messagingTemplate.convertAndSend("/topic/notificaciones", guardada);
    }

    public List<Notificacion> obtenerNotificaciones() {
        return notificacionRepository.findAll();
    }

    public void eliminarNotificacion(int id) {
    notificacionRepository.deleteById(id);
    }

    public void marcarTodasComoLeidas() {
        List<Notificacion> todas = notificacionRepository.findAll();
        todas.forEach(n -> n.setLeido(true));
        notificacionRepository.saveAll(todas);
    }

}
