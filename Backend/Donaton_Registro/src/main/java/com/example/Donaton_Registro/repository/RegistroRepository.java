package com.example.Donaton_Registro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Registro.model.Registro;

public interface RegistroRepository extends JpaRepository<Registro, String> {
    Optional<Registro> findByEmail(String email);

}
