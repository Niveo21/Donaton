package com.example.Donaton_Logistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Logistica.model.Inventario;
import com.example.Donaton_Logistica.repository.InventarioRepository;



@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;


    public void almacenarInventario(Inventario inventario) {
        inventarioRepository.save(inventario);
        
    }

    public List<Inventario> obtenerInventarios() {
        return inventarioRepository.findAll();
    }

    public void restarStock(String articulo, int cantidad) {
        Inventario inventario = inventarioRepository.findByRecurso(articulo)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado en inventario: " + articulo));

        if (inventario.getStockActual() < cantidad) {
            throw new IllegalStateException("Stock insuficiente para '" + articulo +
                "'. Disponible: " + inventario.getStockActual() + ", solicitado: " + cantidad);
        }

        inventario.setStockActual(inventario.getStockActual() - cantidad);
        inventarioRepository.save(inventario);
    }

    public void sumarStock(String articulo, int cantidad) {
        // Buscamos si ya existe el recurso en bodega
        // Despues debo cambiarlo
        inventarioRepository.findByRecurso(articulo).ifPresentOrElse(
            
            inventarioExistente -> {
                inventarioExistente.setStockActual(inventarioExistente.getStockActual() + cantidad);
                inventarioRepository.save(inventarioExistente);
            },
            
            () -> {
                Inventario nuevoInventario = new Inventario();
                
                nuevoInventario.setRecurso(articulo);
                nuevoInventario.setStockActual(cantidad);
                nuevoInventario.setEstado("Disponible");
                inventarioRepository.save(nuevoInventario);
            }
        );
    }

}
