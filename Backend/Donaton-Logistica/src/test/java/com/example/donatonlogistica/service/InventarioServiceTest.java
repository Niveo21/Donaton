package com.example.donatonlogistica.service;

import com.example.donatonlogistica.model.Inventario;
import com.example.donatonlogistica.repository.InventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void sumarStock_conAcopioIdYStockExistente_sumaCantidadAlStockActual() {
        Inventario existente = new Inventario(1, "Agua embotellada", 10, "Disponible");
        existente.setAcopioId(5);

        when(inventarioRepository.findByRecursoAndAcopioId("Agua embotellada", 5))
            .thenReturn(Optional.of(existente));

        inventarioService.sumarStock("Agua embotellada", 4, 5);

        assertEquals(14, existente.getStockActual());
        verify(inventarioRepository).save(existente);
        verify(inventarioRepository, never()).findByRecursoAndAcopioIdIsNull(any());
    }

    @Test
    void sumarStock_conAcopioIdYSinStockPrevio_creaNuevoItemVinculadoAlAcopio() {
        when(inventarioRepository.findByRecursoAndAcopioId("Frazadas", 7))
            .thenReturn(Optional.empty());

        inventarioService.sumarStock("Frazadas", 3, 7);

        ArgumentCaptor<Inventario> captor = ArgumentCaptor.forClass(Inventario.class);
        verify(inventarioRepository).save(captor.capture());

        Inventario creado = captor.getValue();
        assertEquals("Frazadas", creado.getRecurso());
        assertEquals(3, creado.getStockActual());
        assertEquals(7, creado.getAcopioId());
        assertEquals("Disponible", creado.getEstado());
    }

    @Test
    void sumarStock_sinAcopioId_buscaEnStockGeneralNoEnElDeUnAcopio() {
        
        when(inventarioRepository.findByRecursoAndAcopioIdIsNull("Arroz"))
            .thenReturn(Optional.empty());

        inventarioService.sumarStock("Arroz", 10, null);

        verify(inventarioRepository).findByRecursoAndAcopioIdIsNull("Arroz");
        verify(inventarioRepository, never()).findByRecursoAndAcopioId(any(), any());
    }

    @Test
    void restarStock_conStockSuficiente_descuentaCorrectamente() {
        Inventario existente = new Inventario(2, "Pañales", 20, "Disponible");

        when(inventarioRepository.findByRecurso("Pañales")).thenReturn(Optional.of(existente));

        inventarioService.restarStock("Pañales", 8);

        assertEquals(12, existente.getStockActual());
        verify(inventarioRepository).save(existente);
    }

    @Test
    void restarStock_conStockInsuficiente_lanzaIllegalStateException() {
        Inventario existente = new Inventario(3, "Leche en polvo", 2, "Disponible");

        when(inventarioRepository.findByRecurso("Leche en polvo")).thenReturn(Optional.of(existente));

        assertThrows(IllegalStateException.class,
            () -> inventarioService.restarStock("Leche en polvo", 5));

        verify(inventarioRepository, never()).save(any());
    }
}
