package com.example.Donaton_Logistica.repository;

import com.example.Donaton_Logistica.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario> findByEnvioId(int envioId);
}
