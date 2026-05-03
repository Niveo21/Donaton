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

    public void sumarStock(String articulo, int cantidad) {
        // Buscamos si ya existe el recurso en bodega
        // Despues debo cambiarlo por if convencional
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
