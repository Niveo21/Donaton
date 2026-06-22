package com.example.Donaton_Notificaciones.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Notificaciones.model.MensajeChat;
import com.example.Donaton_Notificaciones.repository.MensajeChatRepository;

@Service
public class ChatService {

    @Autowired
    private MensajeChatRepository mensajeChatRepository;

    public MensajeChat guardarMensaje(MensajeChat mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());
        return mensajeChatRepository.save(mensaje);
    }

    public List<MensajeChat> obtenerHistorial() {
        return mensajeChatRepository.findAll();
    }
}
