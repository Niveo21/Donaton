package com.example.donatonlogistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.donatonlogistica.model.Voluntario;

public interface VoluntarioRepository extends JpaRepository<Voluntario, String> {

    List<Voluntario> findByTransporteId(int transporteId);

    List<Voluntario> findByAcopioId(int acopioId);
}
