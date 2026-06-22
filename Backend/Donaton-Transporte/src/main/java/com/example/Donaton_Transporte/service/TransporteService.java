package com.example.Donaton_Transporte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Transporte.model.Transporte;
import com.example.Donaton_Transporte.repository.TransporteRepository;

@Service
public class TransporteService {

    @Autowired
    private TransporteRepository transporteRepository;

    public String almacenarTransporte(Transporte transporte) {
        if (transporteRepository.findByPlaca(transporte.getPlaca()).isPresent()) {
            return "La placa ya está registrada";
        }
        transporteRepository.save(transporte);
        return "Transporte almacenado correctamente";
    }

    public List<Transporte> obtenerTransportes() {
        return transporteRepository.findAll();
    }

    public Transporte obtenerPorId(int id) {
        return transporteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transporte no encontrado: " + id));
    }

    public void eliminarTransporte(int id) {
        transporteRepository.deleteById(id);
    }
}
