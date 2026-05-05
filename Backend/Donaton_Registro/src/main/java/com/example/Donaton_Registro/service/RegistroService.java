package com.example.Donaton_Registro.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Donaton_Registro.dto.RegistroRequestDTO;
import com.example.Donaton_Registro.model.Registro;
import com.example.Donaton_Registro.repository.RegistroRepository;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    /**
     * Registra un nuevo usuario aplicando todas las validaciones de negocio:
     * - Contraseñas coincidentes
     * - RUT con dígito verificador correcto
     * - RUT y email únicos en el sistema
     */
    public Registro almacenarRegistro(RegistroRequestDTO dto) {

        // 1. Verificar que las contraseñas coincidan
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        // 2. Validar dígito verificador del RUT chileno
        if (!validarRutChileno(dto.getRut())) {
            throw new IllegalArgumentException(
                "El RUT '" + dto.getRut() + "' tiene un dígito verificador incorrecto"
            );
        }

        // 3. Verificar que el RUT no esté ya registrado
        if (registroRepository.existsByRut(dto.getRut())) {
            throw new DuplicateResourceException("El RUT '" + dto.getRut() + "' ya se encuentra registrado");
        }

        // 4. Verificar que el email no esté ya registrado
        if (registroRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("El email '" + dto.getEmail() + "' ya se encuentra registrado");
        }

        // 5. Mapear DTO → entidad y guardar
        Registro registro = new Registro(
            dto.getRut(),
            dto.getNombre(),
            dto.getEmail(),
            dto.getPassword(),   // Sin encriptar (paso 4 omitido por decisión de diseño)
            dto.getRol()
        );

        return registroRepository.save(registro);
    }

    public List<Registro> obtenerRegistros() {
        return registroRepository.findAll();
    }

    public Registro obtenerPorRut(String rut) {
        return registroRepository.findById(rut)
            .orElseThrow(() -> new IllegalArgumentException(
                "No se encontró ningún registro con el RUT: " + rut
            ));
    }

    // ─────────────────────────────────────────────────────────────
    //  Algoritmo de validación de RUT chileno (módulo 11)
    // ─────────────────────────────────────────────────────────────

    /**
     * Valida el dígito verificador de un RUT chileno.
     * Formato esperado: "12345678-9" o "12345678-K"
     */
    private boolean validarRutChileno(String rut) {
        if (rut == null || !rut.contains("-")) {
            return false;
        }

        String[] partes = rut.split("-");
        if (partes.length != 2) {
            return false;
        }

        String cuerpo = partes[0];
        String dvIngresado = partes[1].toUpperCase();

        // El cuerpo solo debe contener dígitos
        if (!cuerpo.matches("[0-9]+")) {
            return false;
        }

        String dvCalculado = calcularDigitoVerificador(cuerpo);
        return dvCalculado.equals(dvIngresado);
    }

    /**
     * Calcula el dígito verificador a partir del cuerpo del RUT (sin guión).
     * Retorna "K" si el resultado es 10, "0" si es 11, o el número como string.
     */
    private String calcularDigitoVerificador(String cuerpo) {
        int suma = 0;
        int multiplicador = 2;

        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = suma % 11;
        if (resto == 0) return "0";
        if (resto == 1) return "K";
        return String.valueOf(11 - resto);
    }

    // ─────────────────────────────────────────────────────────────
    //  Excepción para recursos duplicados (409 Conflict)
    // ─────────────────────────────────────────────────────────────

    public static class DuplicateResourceException extends RuntimeException {
        public DuplicateResourceException(String message) {
            super(message);
        }
    }
}
