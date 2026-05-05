package com.example.Donaton_Registro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Registro.model.Registro;

public interface RegistroRepository extends JpaRepository<Registro, String> {

    /** Verifica si ya existe un registro con ese email. */
    boolean existsByEmail(String email);

    /** Verifica si ya existe un registro con ese RUT. */
    boolean existsByRut(String rut);

    /** Busca un registro por email (útil para login futuro). */
    java.util.Optional<Registro> findByEmail(String email);
}
