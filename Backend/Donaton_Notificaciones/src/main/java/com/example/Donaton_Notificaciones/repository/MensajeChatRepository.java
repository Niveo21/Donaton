package com.example.Donaton_Notificaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Notificaciones.model.MensajeChat;

public interface MensajeChatRepository extends JpaRepository<MensajeChat, Integer> {
}
