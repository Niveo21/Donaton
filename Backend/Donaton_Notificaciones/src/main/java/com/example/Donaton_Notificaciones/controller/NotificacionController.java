package com.example.Donaton_Notificaciones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Donaton_Notificaciones.model.Notificacion;
import com.example.Donaton_Notificaciones.service.NotificacionService;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/notificacion")
public class NotificacionController {


    @Autowired
    private NotificacionService notificacionService;
    

    @PostMapping
    public String almacenarNotificacion(@RequestBody Notificacion notificacion) {
        notificacionService.almacenarNotificacion(notificacion);
        return "Notificación almacenada correctamente";
    }

    @GetMapping
    public List<Notificacion> obtenerNotificaciones() {
        return notificacionService.obtenerNotificaciones();
    }

    @PutMapping("/marcar-leidas")
    public String marcarTodasComoLeidas() {
        notificacionService.marcarTodasComoLeidas();
        return "Notificaciones marcadas como leídas";
    }

    @DeleteMapping("/{id}")
    public String eliminarNotificacion(@PathVariable int id) {
    notificacionService.eliminarNotificacion(id);
    return "Notificación eliminada correctamente";
}

}
