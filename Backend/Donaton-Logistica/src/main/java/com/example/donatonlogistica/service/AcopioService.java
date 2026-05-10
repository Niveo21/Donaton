package com.example.donatonlogistica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.donatonlogistica.model.Acopio;
import com.example.donatonlogistica.repository.AcopioRepository;


@Service
public class AcopioService {

    @Autowired
    private AcopioRepository acopioRepository;


    public void almacenarAcopio(Acopio acopio) {
        acopioRepository.save(acopio);
        
    }

    public List<Acopio> obtenerAcopios() {
        return acopioRepository.findAll();
    }

}
