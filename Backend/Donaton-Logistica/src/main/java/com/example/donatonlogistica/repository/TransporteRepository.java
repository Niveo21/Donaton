package com.example.donatonlogistica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.donatonlogistica.model.Transporte;

public interface TransporteRepository extends JpaRepository<Transporte, Integer> {

    Optional<Transporte> findByPlaca(String placa);

}
