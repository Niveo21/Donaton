package com.example.Donaton_Registro.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.repository.RegistroRepository;

/**
 * Crea automáticamente una cuenta de administrador al arrancar el servicio,
 * si todavía no existe. No se duplica en reinicios posteriores.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@donaton.cl";
    private static final String ADMIN_PASSWORD = "admin123";

    @Autowired
    private RegistroRepository registroRepository;

    @Override
    public void run(String... args) {
        if (registroRepository.findByEmail(ADMIN_EMAIL).isPresent()) {
            return;
        }

        Registro admin = new Registro();
        admin.setRut("11111111-1");
        admin.setNombre("Administrador");
        admin.setEmail(ADMIN_EMAIL);
        admin.setPassword(ADMIN_PASSWORD);
        admin.setRol("ADMIN");
        registroRepository.save(admin);

        System.out.println("Cuenta de administrador creada automáticamente: " + ADMIN_EMAIL + " / " + ADMIN_PASSWORD);
    }
}
