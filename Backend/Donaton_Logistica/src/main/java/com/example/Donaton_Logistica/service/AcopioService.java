package com.example.Donaton_Logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Logistica.model.Acopio;
import com.example.Donaton_Logistica.repository.AcopioRepository;


@Service
public class AcopioService {

    @Autowired
    private AcopioRepository acopioRepository;


    public void almacenarAcopio(Acopio acopio) {
        acopioRepository.save(acopio);
        
    }

    public List<Acopio> obtenerAcopios() {
        return acopioRepository.findAll();
    }

}
