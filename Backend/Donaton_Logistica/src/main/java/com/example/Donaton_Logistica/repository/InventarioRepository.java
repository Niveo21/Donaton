package com.example.Donaton_Logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Logistica.model.Inventario;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    Optional<Inventario> findByRecurso(String recurso);

}
