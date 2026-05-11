const express = require('express');
const axios = require('axios');

const router = express.Router();

const NECESIDAD_URL = 'http://localhost:8082/necesidad';

// GET /necesidad — Listar todas las necesidades
router.get('/', async (req, res) => {
    try {
        console.log("Consultando microservicio de Necesidad...");
        const response = await axios.get(NECESIDAD_URL);

        const dataOriginal = response.data;
        const dataLimpia = dataOriginal.map(item => ({
            id: item.id,
            descripcion: item.descripcion,
            tipoRecurso: item.tipoRecurso,
            cantidadRequerida: item.cantidadRequerida,
            estado: item.estado
        }));

        res.json(dataLimpia);
    } catch (error) {
        console.error("Error contactando microservicio de Necesidad:", error.message);
        res.status(500).json({ error: "Fallo de comunicación con el microservicio de Necesidad" });
    }
});

// POST /necesidad — Registrar una nueva necesidad
router.post('/', async (req, res) => {
    try {
        console.log("Registrando necesidad en microservicio...");
        const response = await axios.post(NECESIDAD_URL, req.body);
        return res.status(201).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en el microservicio de Necesidad:", error.message);
        res.status(500).json({ error: "No se pudo registrar la necesidad" });
    }
});

module.exports = router;
