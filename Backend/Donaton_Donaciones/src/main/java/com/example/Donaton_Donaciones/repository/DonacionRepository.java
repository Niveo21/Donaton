package com.example.Donaton_Donaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Donaton_Donaciones.model.Donacion;

public interface DonacionRepository extends JpaRepository<Donacion, Integer> {

}
