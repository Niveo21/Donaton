package com.example.Donaton_Logistica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Logistica.model.Voluntario;

public interface VoluntarioRepository extends JpaRepository<Voluntario, String> {

    List<Voluntario> findByTransporteId(int transporteId);

    List<Voluntario> findByAcopioId(int acopioId);
}
