package com.example.Donaton_Logistica.controller;

import com.example.Donaton_Logistica.model.Envio;
import com.example.Donaton_Logistica.model.MovimientoInventario;
import com.example.Donaton_Logistica.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/envio")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    public ResponseEntity<?> crearEnvio(@RequestBody Map<String, Object> body) {
        try {
            int transporteId = (int) body.get("transporteId");
            int acopioId = (int) body.get("acopioId");
            String tipoRecurso = (String) body.get("tipoRecurso");
            int cantidad = (int) body.get("cantidad");

            Envio envio = envioService.crearEnvio(transporteId, acopioId, tipoRecurso, cantidad);
            return ResponseEntity.ok(envio);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Envio> obtenerEnvios() {
        return envioService.obtenerEnvios();
    }

    @GetMapping("/{id}/movimientos")
    public ResponseEntity<?> movimientosPorEnvio(@PathVariable int id) {
        List<MovimientoInventario> movimientos = envioService.obtenerMovimientosPorEnvio(id);
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/movimientos")
    public List<MovimientoInventario> todosLosMovimientos() {
        return envioService.obtenerTodosLosMovimientos();
    }
}
