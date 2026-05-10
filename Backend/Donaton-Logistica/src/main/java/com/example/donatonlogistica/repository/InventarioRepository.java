package com.example.donatonlogistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.donatonlogistica.model.Inventario;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    Optional<Inventario> findByRecurso(String recurso);

}
