package com.example.Donaton_Registro.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Donaton_Registro.dto.TokenResponse;

@Service
public class AuthClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public String generarToken(String rut, String rol) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("rut", rut);
            body.put("rol", rol);

            String url = authServiceUrl + "/auth/generar";
            TokenResponse respuesta = restTemplate.postForObject(url, body, TokenResponse.class);
            return respuesta != null ? respuesta.getToken() : null;
        } catch (Exception e) {
            System.out.println("Aviso: no se pudo generar el token de autenticación: " + e.getMessage());
            return null;
        }
    }
}
