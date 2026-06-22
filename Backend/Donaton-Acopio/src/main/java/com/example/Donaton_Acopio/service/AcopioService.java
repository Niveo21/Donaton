package com.example.Donaton_Acopio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Acopio.model.Acopio;
import com.example.Donaton_Acopio.repository.AcopioRepository;

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

    public Acopio obtenerPorId(int id) {
        return acopioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Acopio no encontrado: " + id));
    }

    public void eliminarAcopio(int id) {
        acopioRepository.deleteById(id);
    }
}
