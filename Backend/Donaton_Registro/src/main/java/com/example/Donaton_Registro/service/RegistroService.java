package com.example.Donaton_Registro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.example.Donaton_Registro.dto.LoginResponse;
import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.repository.RegistroRepository;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthClientService authClient;

    public String almacenarRegistro(Registro registro) {

    if (registroRepository.findByEmail(registro.getEmail()).isPresent()) {
        return "El email ya está registrado";
    }

    if (registroRepository.findById(registro.getRut()).isPresent()) {
        return "El RUT ya está registrado";
    }

    if ("EMPRESA".equalsIgnoreCase(registro.getRol())) {
    if (registro.getRazonSocial() == null || registro.getRazonSocial().isBlank())
        return "La razón social es obligatoria";

    if (registro.getGiro() == null || registro.getGiro().isBlank())
        return "El giro comercial es obligatorio";

    if (registro.getTelefono() == null || registro.getTelefono().isBlank())
        return "El teléfono es obligatorio";
}   
    if("VOLUNTARIO".equalsIgnoreCase(registro.getRol())) {
        if (registro.getEdad() < 18)
            return "La edad mínima para ser voluntario es 18 años";

        if (registro.getRegion() == null || registro.getRegion().isBlank())
            return "La región es obligatoria";
    }
        registroRepository.save(registro);
        actualizarVoluntario(registro);
        return "Registro almacenado correctamente";
    }


    public List<Registro> obtenerRegistros() {
        return registroRepository.findAll();
    }


    public LoginResponse login(String email, String password) {
        Registro registro = registroRepository.findByEmail(email).orElse(null);
        if (registro == null || !registro.getPassword().equals(password)) {
            return null;
        }
        String token = authClient.generarToken(registro.getRut(), registro.getRol());
        return new LoginResponse(registro.getRut(), registro.getNombre(), registro.getEmail(), registro.getRol(), token);
    }


    public Registro obtenerPorEmail(String email) {
        return registroRepository.findByEmail(email).orElse(null);
    }
 
    public void actualizarVoluntario(Registro registro) {
    if ("Voluntario".equalsIgnoreCase(registro.getRol())) {
        try {
            String url = "http://localhost:8081/voluntario/actualizar";
            restTemplate.postForObject(url, registro, Void.class);
        } catch (Exception e) {
            
            System.out.println("Aviso: no se pudo notificar a Logística: " + e.getMessage());
        }
    }
}

}