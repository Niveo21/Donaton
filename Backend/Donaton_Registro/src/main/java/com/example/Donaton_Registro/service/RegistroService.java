package com.example.Donaton_Registro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.repository.RegistroRepository;

@Service
public class RegistroService {


    @Autowired
    private RegistroRepository registroRepository;

    public void almacenarRegistro(Registro registro) {
        registroRepository.save(registro);
    }

    public List<Registro> obtenerRegistros() {
        return registroRepository.findAll();
    }

}
