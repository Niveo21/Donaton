package com.example.donatonlogistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.donatonlogistica.model.Inventario;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    Optional<Inventario> findByRecurso(String recurso);

    List<Inventario> findByAcopioId(Integer acopioId);

    Optional<Inventario> findByRecursoAndAcopioId(String recurso, Integer acopioId);

    Optional<Inventario> findByRecursoAndAcopioIdIsNull(String recurso);

}
