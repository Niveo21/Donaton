package com.example.donatonlogistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.donatonlogistica.dto.AcopioDTO;
import com.example.donatonlogistica.dto.TransporteDTO;
import com.example.donatonlogistica.dto.VoluntarioDTO;
import com.example.donatonlogistica.model.Voluntario;
import com.example.donatonlogistica.repository.VoluntarioRepository;


@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void almacenarVoluntario(Voluntario voluntario) {
        voluntarioRepository.save(voluntario);
    }

    public List<VoluntarioDTO> obtenerVoluntarios() {
        return voluntarioRepository.findAll().stream()
                .map(this::enriquecer)
                .toList();
    }

    public VoluntarioDTO asignarTransporte(String rut, int transporteId) {
        Voluntario voluntario = voluntarioRepository.findById(rut)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + rut));
        // Transporte ahora vive en su propio microservicio; solo guardamos el id.
        voluntario.setTransporteId(transporteId);
        Voluntario guardado = voluntarioRepository.save(voluntario);
        return enriquecer(guardado);
    }

    public VoluntarioDTO asignarAcopio(String rut, int acopioId) {
        Voluntario voluntario = voluntarioRepository.findById(rut)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario no encontrado: " + rut));
        // Acopio ahora vive en su propio microservicio; solo guardamos el id.
        voluntario.setAcopioId(acopioId);
        Voluntario guardado = voluntarioRepository.save(voluntario);
        return enriquecer(guardado);
    }

    public List<VoluntarioDTO> obtenerPorTransporte(int transporteId) {
        return voluntarioRepository.findByTransporteId(transporteId).stream()
                .map(this::enriquecer)
                .toList();
    }

    public List<VoluntarioDTO> obtenerPorAcopio(int acopioId) {
        return voluntarioRepository.findByAcopioId(acopioId).stream()
                .map(this::enriquecer)
                .toList();
    }

    private VoluntarioDTO enriquecer(Voluntario voluntario) {
        VoluntarioDTO dto = new VoluntarioDTO(voluntario);
        if (voluntario.getTransporteId() != null) {
            dto.setTransporte(resolverTransporte(voluntario.getTransporteId()));
        }
        if (voluntario.getAcopioId() != null) {
            dto.setAcopio(resolverAcopio(voluntario.getAcopioId()));
        }
        return dto;
    }

    private TransporteDTO resolverTransporte(int transporteId) {
        try {
            String url = "http://DONATON-TRANSPORTE/transporte/" + transporteId;
            return restTemplate.getForObject(url, TransporteDTO.class);
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo obtener el transporte " + transporteId + ": " + e.getMessage());
            return null;
        }
    }

    private AcopioDTO resolverAcopio(int acopioId) {
        try {
            String url = "http://DONATON-ACOPIO/acopio/" + acopioId;
            return restTemplate.getForObject(url, AcopioDTO.class);
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo obtener el acopio " + acopioId + ": " + e.getMessage());
            return null;
        }
    }
}
