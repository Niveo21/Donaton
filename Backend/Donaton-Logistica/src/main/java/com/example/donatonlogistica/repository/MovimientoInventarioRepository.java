package com.example.donatonlogistica.repository;

import com.example.donatonlogistica.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {

    List<MovimientoInventario> findByEnvioId(int envioId);
}
