package com.example.Donaton_Notificaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Notificaciones.model.MensajeChat;
import com.example.Donaton_Notificaciones.service.ChatService;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public List<MensajeChat> obtenerHistorial() {
        return chatService.obtenerHistorial();
    }

    @MessageMapping("/chat.enviar")
    @SendTo("/topic/chat")
    public MensajeChat enviarMensaje(MensajeChat mensaje) {
        return chatService.guardarMensaje(mensaje);
    }
}
