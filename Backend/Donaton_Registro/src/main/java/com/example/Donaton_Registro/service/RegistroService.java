package com.example.Donaton_Registro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.repository.RegistroRepository;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String almacenarRegistro(Registro registro) {
        
        if (registroRepository.findByEmail(registro.getEmail()).isPresent()) {
            return "El email ya está registrado";
            
        }

        if (registroRepository.findById(registro.getRut()).isPresent()) {
            return "El RUT ya está registrado";
            
        }else {
            registroRepository.save(registro);
            actualizarVoluntario(registro);
            return "Registro almacenado correctamente";
        }
    }


    public List<Registro> obtenerRegistros() {
        return registroRepository.findAll();
    }


    public boolean validarLogin(String email, String password) {
        Registro registro = registroRepository.findByEmail(email).orElse(null);
        if (registro != null && registro.getPassword().equals(password)) {
            return true;
        }
        return false;
    }


    public Registro obtenerPorEmail(String email) {
        return registroRepository.findByEmail(email).orElse(null);
    }
 
    public void actualizarVoluntario(Registro registro) {


        if ("Voluntario".equalsIgnoreCase(registro.getRol())) {
            String url = "http://localhost:8081/voluntario/actualizar";
            restTemplate.postForObject(url, registro, Void.class);
            
        }
        
    }

    
}
