package com.example.Donaton_Notificaciones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Notificaciones.model.Notificacion;
import com.example.Donaton_Notificaciones.repository.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;


    public void almacenarNotificacion(Notificacion notificacion) {
        notificacionRepository.save(notificacion);
    }

    public List<Notificacion> obtenerNotificaciones() {
        return notificacionRepository.findAll();
    }

}
