package com.example.Donaton_Logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Logistica.model.Acopio;
import com.example.Donaton_Logistica.model.Transporte;
import com.example.Donaton_Logistica.model.Voluntario;
import com.example.Donaton_Logistica.repository.AcopioRepository;
import com.example.Donaton_Logistica.repository.TransporteRepository;
import com.example.Donaton_Logistica.repository.VoluntarioRepository;


@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private AcopioRepository acopioRepository;

    public void almacenarVoluntario(Voluntario voluntario) {
        voluntarioRepository.save(voluntario);
    }

    public List<Voluntario> obtenerVoluntarios() {
        return voluntarioRepository.findAll();
    }

    public Voluntario asignarTransporte(String rut, int transporteId) {
        Voluntario voluntario = voluntarioRepository.findById(rut)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + rut));
        Transporte transporte = transporteRepository.findById(transporteId)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado: " + transporteId));
        voluntario.setTransporte(transporte);
        return voluntarioRepository.save(voluntario);
    }

    public Voluntario asignarAcopio(String rut, int acopioId) {
        Voluntario voluntario = voluntarioRepository.findById(rut)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + rut));
        Acopio acopio = acopioRepository.findById(acopioId)
                .orElseThrow(() -> new IllegalArgumentException("Acopio no encontrado: " + acopioId));
        voluntario.setAcopio(acopio);
        return voluntarioRepository.save(voluntario);
    }

    public List<Voluntario> obtenerPorTransporte(int transporteId) {
        return voluntarioRepository.findByTransporteId(transporteId);
    }

    public List<Voluntario> obtenerPorAcopio(int acopioId) {
        return voluntarioRepository.findByAcopioId(acopioId);
    }
}
