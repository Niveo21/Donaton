package com.example.Donaton_Necesidad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Necesidad.model.Necesidad;
import com.example.Donaton_Necesidad.repository.NecesidadRepository;

@Service
public class NecesidadService {

    @Autowired
    private NecesidadRepository necesidadRepository;

    
    public void almacenarNecesidad(Necesidad necesidad) {
        necesidadRepository.save(necesidad);
    }

    public List<Necesidad> obtenerNecesidades() {
        return necesidadRepository.findAll();
    }

}
