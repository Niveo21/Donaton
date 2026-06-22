const express = require('express');
const axios = require('axios');

const router = express.Router();

const NOTIFICACION_URL = 'http://localhost:8083/notificacion';

// GET /notificacion — Listar todas las notificaciones
router.get('/', async (req, res) => {
    try {
        console.log("Consultando microservicio de Notificaciones...");
        const response = await axios.get(NOTIFICACION_URL);

        const dataOriginal = response.data;
        const dataLimpia = dataOriginal.map(item => ({
            id: item.id,
            mensaje: item.mensaje,
            leido: item.leido
        }));

        res.json(dataLimpia);
    } catch (error) {
        console.error("Error contactando microservicio de Notificaciones:", error.message);
        res.status(500).json({ error: "Fallo de comunicación con el microservicio de Notificaciones" });
    }
});

// POST /notificacion — Crear una notificación
router.post('/', async (req, res) => {
    try {
        console.log("Enviando notificación al microservicio...");
        const response = await axios.post(NOTIFICACION_URL, req.body);
        return res.status(201).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en el microservicio de Notificaciones:", error.message);
        res.status(500).json({ error: "No se pudo crear la notificación" });
    }
});

// PUT /notificacion/marcar-leidas — Marcar todas como leídas
router.put('/marcar-leidas', async (req, res) => {
    try {
        const response = await axios.put(`${NOTIFICACION_URL}/marcar-leidas`);
        return res.status(200).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error al marcar notificaciones como leídas:", error.message);
        res.status(500).json({ error: "No se pudo actualizar las notificaciones" });
    }
});

module.exports = router;
