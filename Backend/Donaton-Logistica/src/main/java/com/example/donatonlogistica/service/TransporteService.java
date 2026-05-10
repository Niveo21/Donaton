package com.example.donatonlogistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.donatonlogistica.model.Transporte;
import com.example.donatonlogistica.repository.TransporteRepository;

@Service
public class TransporteService {

    @Autowired
    private TransporteRepository transporteRepository;

    public String AlmacenarTransporte(Transporte transporte) {

        if (transporteRepository.findByPlaca(transporte.getPlaca()).isPresent()) {
            return "La placa ya está registrada";
        }
        transporteRepository.save(transporte);
        return "Transporte almacenado correctamente";
    }

    public List<Transporte> obtenerTransportes() {
        return transporteRepository.findAll();
    }

}
