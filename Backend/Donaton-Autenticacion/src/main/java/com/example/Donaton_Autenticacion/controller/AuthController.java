package com.example.Donaton_Autenticacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Autenticacion.dto.TokenRequest;
import com.example.Donaton_Autenticacion.dto.TokenResponse;
import com.example.Donaton_Autenticacion.dto.ValidarTokenRequest;
import com.example.Donaton_Autenticacion.dto.ValidarTokenResponse;
import com.example.Donaton_Autenticacion.service.JwtService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/generar")
    public TokenResponse generarToken(@RequestBody TokenRequest request) {
        String token = jwtService.generarToken(request.getRut(), request.getRol());
        return new TokenResponse(token);
    }

    @PostMapping("/validar")
    public ValidarTokenResponse validarToken(@RequestBody ValidarTokenRequest request) {
        Claims claims = jwtService.validarToken(request.getToken());
        if (claims == null) {
            return new ValidarTokenResponse(false, null, null);
        }
        return new ValidarTokenResponse(true, claims.getSubject(), claims.get("rol", String.class));
    }
}
