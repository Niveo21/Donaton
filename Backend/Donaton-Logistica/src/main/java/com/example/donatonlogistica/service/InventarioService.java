package com.example.donatonlogistica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.donatonlogistica.model.Inventario;
import com.example.donatonlogistica.repository.InventarioRepository;



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

    public List<Inventario> obtenerInventarioPorAcopio(Integer acopioId) {
        return inventarioRepository.findByAcopioId(acopioId);
    }

    public void agregarItemAcopio(Integer acopioId, String recurso, int cantidad) {
        Inventario item = new Inventario();
        item.setAcopioId(acopioId);
        item.setRecurso(recurso);
        item.setStockActual(cantidad);
        item.setEstado("Disponible");
        inventarioRepository.save(item);
    }

    public void actualizarItem(Integer id, Inventario datos) {
        Inventario item = inventarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Item de inventario no encontrado: " + id));
        item.setRecurso(datos.getRecurso());
        item.setStockActual(datos.getStockActual());
        inventarioRepository.save(item);
    }

    public void eliminarItem(Integer id) {
        inventarioRepository.deleteById(id);
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

    public void sumarStock(String articulo, int cantidad, Integer acopioId) {
        // Si viene con acopioId, el stock se acumula en el inventario de ESE punto.
        // Si no (ej. donación a domicilio sin punto asignado), se usa el stock general.
        Optional<Inventario> existente = acopioId != null
            ? inventarioRepository.findByRecursoAndAcopioId(articulo, acopioId)
            : inventarioRepository.findByRecursoAndAcopioIdIsNull(articulo);

        existente.ifPresentOrElse(
            inventarioExistente -> {
                inventarioExistente.setStockActual(inventarioExistente.getStockActual() + cantidad);
                inventarioRepository.save(inventarioExistente);
            },
            () -> {
                Inventario nuevoInventario = new Inventario();
                nuevoInventario.setRecurso(articulo);
                nuevoInventario.setStockActual(cantidad);
                nuevoInventario.setEstado("Disponible");
                nuevoInventario.setAcopioId(acopioId);
                inventarioRepository.save(nuevoInventario);
            }
        );
    }

}
