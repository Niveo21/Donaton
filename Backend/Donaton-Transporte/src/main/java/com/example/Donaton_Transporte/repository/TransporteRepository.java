package com.example.Donaton_Transporte.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Transporte.model.Transporte;

public interface TransporteRepository extends JpaRepository<Transporte, Integer> {

    Optional<Transporte> findByPlaca(String placa);
}
