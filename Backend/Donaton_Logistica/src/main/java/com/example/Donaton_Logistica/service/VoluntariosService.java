package com.example.Donaton_Logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Logistica.model.Voluntarios;
import com.example.Donaton_Logistica.repository.VoluntariosRepository;

@Service
public class VoluntariosService {


    @Autowired
    private VoluntariosRepository voluntariosRepository;

    public void almacenarVoluntarios(Voluntarios voluntarios) {
        voluntariosRepository.save(voluntarios);
        
    }

    public List<Voluntarios> obtenerVoluntarios() {
        return voluntariosRepository.findAll();
    }

}
