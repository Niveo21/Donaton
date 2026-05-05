package com.example.Donaton_Registro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Registro.dto.RegistroRequestDTO;
import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.service.RegistroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private RegistroService registroService;

    /**
     * POST /registro
     * Crea un nuevo registro. Devuelve 201 Created con el objeto guardado.
     * Si los datos no son válidos, el GlobalExceptionHandler retorna 400/409.
     */
    @PostMapping
    public ResponseEntity<Registro> almacenarRegistro(@Valid @RequestBody RegistroRequestDTO dto) {
        Registro registroGuardado = registroService.almacenarRegistro(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registroGuardado);
    }

    /**
     * GET /registro
     * Retorna la lista completa de registros.
     */
    @GetMapping
    public ResponseEntity<List<Registro>> obtenerRegistros() {
        return ResponseEntity.ok(registroService.obtenerRegistros());
    }

    /**
     * GET /registro/{rut}
     * Retorna el registro asociado al RUT indicado.
     */
    @GetMapping("/{rut}")
    public ResponseEntity<Registro> obtenerPorRut(@PathVariable String rut) {
        Registro registro = registroService.obtenerPorRut(rut);
        return ResponseEntity.ok(registro);
    }
}
