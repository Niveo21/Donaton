package com.example.Donaton_Donaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Donaton_Donaciones.model.Donacion;
import com.example.Donaton_Donaciones.repository.DonacionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * Servicio separado para la comunicación con el microservicio de Logística.
 * El @CircuitBreaker funciona correctamente aquí porque Spring puede
 * interceptar la llamada a través del proxy (no es una auto-invocación).
 */
@Service
public class LogisticaClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DonacionRepository donacionRepository;

    @CircuitBreaker(name = "inventarioCB", fallbackMethod = "inventarioFallback")
    public void notificarLogistica(Donacion donacion) {
        String url = "http://localhost:8081/inventario/actualizar";
        restTemplate.postForObject(url, donacion, Void.class);
    }

    public void inventarioFallback(Donacion donacion, Throwable exception) {
        // La donación YA está guardada, solo actualizamos el estado
        donacion.setEstado("Recibida - Pendiente inventario");
        donacionRepository.save(donacion);

        System.out.println("¡Alerta! El inventario está caído. La donación " + donacion.getId() +
                " se guardó, pero el inventario no se ha actualizado. " + exception.getMessage());
    }
}
