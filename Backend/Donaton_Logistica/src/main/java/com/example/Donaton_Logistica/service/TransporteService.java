package com.example.Donaton_Logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Logistica.model.Transporte;
import com.example.Donaton_Logistica.repository.TransporteRepository;

@Service
public class TransporteService {

    @Autowired
    private TransporteRepository transporteRepository;

    public void AlmacenarTransporte(Transporte transporte) {
        transporteRepository.save(transporte);
    }

    public List<Transporte> obtenerTransportes() {
        return transporteRepository.findAll();
    }

}
