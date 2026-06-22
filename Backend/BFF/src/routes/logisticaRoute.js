const express = require('express');
const axios = require('axios');

const router = express.Router();

const LOGISTICA_URL = process.env.LOGISTICA_URL || 'http://localhost:8081';
const TRANSPORTE_URL = process.env.TRANSPORTE_URL || 'http://localhost:8086';
const ACOPIO_URL = process.env.ACOPIO_URL || 'http://localhost:8087';

// ACOPIO
// GET /acopio
router.get('/acopio', async (req, res) => {
    try {
        const response = await axios.get(`${ACOPIO_URL}/acopio`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en Acopio GET:", error.message);
        res.status(500).json({ error: "Error al obtener acopios" });
    }
});

// POST /acopio
router.post('/acopio', async (req, res) => {
    try {
        const response = await axios.post(`${ACOPIO_URL}/acopio`, req.body);
        return res.status(201).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en Acopio POST:", error.message);
        res.status(500).json({ error: "Error al registrar acopio" });
    }
})

// DELETE /acopio/:id
router.delete('/acopio/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const response = await axios.delete(`${ACOPIO_URL}/acopio/${id}`);
        res.json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en Acopio DELETE:", error.message);
        res.status(500).json({ error: "Error al eliminar acopio" });
    }
});

// ENVIO


// GET /envio
router.get('/envio', async (req, res) => {
    try {
        const response = await axios.get(`${LOGISTICA_URL}/envio`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en Envio GET:", error.message);
        res.status(500).json({ error: "Error al obtener envíos" });
    }
});

// POST /envio
router.post('/envio', async (req, res) => {
    try {
        const response = await axios.post(`${LOGISTICA_URL}/envio`, req.body);
        return res.status(201).json(response.data);
    } catch (error) {
        console.error("Error en Envio POST:", error.message);
        res.status(500).json({ error: "Error al crear envío" });
    }
});

// GET /envio/:id/movimientos
router.get('/envio/:id/movimientos', async (req, res) => {
    try {
        const { id } = req.params;
        const response = await axios.get(`${LOGISTICA_URL}/envio/${id}/movimientos`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en movimientos por envío:", error.message);
        res.status(500).json({ error: "Error al obtener movimientos del envío" });
    }
});

// GET /envio/movimientos
router.get('/envio/movimientos', async (req, res) => {
    try {
        const response = await axios.get(`${LOGISTICA_URL}/envio/movimientos`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en todos los movimientos:", error.message);
        res.status(500).json({ error: "Error al obtener todos los movimientos" });
    }
});


// INVENTARIO


// GET /inventario
router.get('/inventario', async (req, res) => {
    try {
        const response = await axios.get(`${LOGISTICA_URL}/inventario`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en Inventario GET:", error.message);
        res.status(500).json({ error: "Error al obtener inventario" });
    }
});

// POST /inventario
router.post('/inventario', async (req, res) => {
    try {
        const response = await axios.post(`${LOGISTICA_URL}/inventario`, req.body);
        return res.status(201).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en Inventario POST:", error.message);
        res.status(500).json({ error: "Error al registrar inventario" });
    }
});

// POST /inventario/actualizar
router.post('/inventario/actualizar', async (req, res) => {
    try {
        const response = await axios.post(`${LOGISTICA_URL}/inventario/actualizar`, req.body);
        return res.status(200).json({ mensaje: "Inventario actualizado" });
    } catch (error) {
        console.error("Error actualizando inventario:", error.message);
        res.status(500).json({ error: "Error al actualizar inventario" });
    }
});

// TRANSPORTE


// GET /transporte
router.get('/transporte', async (req, res) => {
    try {
        const response = await axios.get(`${TRANSPORTE_URL}/transporte`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en Transporte GET:", error.message);
        res.status(500).json({ error: "Error al obtener transportes" });
    }
});

// POST /transporte
router.post('/transporte', async (req, res) => {
    try {
        const response = await axios.post(`${TRANSPORTE_URL}/transporte`, req.body);
        return res.status(201).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en Transporte POST:", error.message);
        res.status(500).json({ error: "Error al registrar transporte" });
    }
});

// DELETE /transporte/:id
router.delete('/transporte/:id', async (req, res) => {
    try {
        const { id } = req.params;
        const response = await axios.delete(`${TRANSPORTE_URL}/transporte/${id}`);
        res.json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en Transporte DELETE:", error.message);
        res.status(500).json({ error: "Error al eliminar transporte" });
    }
});


// VOLUNTARIO


// GET /voluntario
router.get('/voluntario', async (req, res) => {
    try {
        const response = await axios.get(`${LOGISTICA_URL}/voluntario`);
        res.json(response.data);
    } catch (error) {
        console.error("Error en Voluntario GET:", error.message);
        res.status(500).json({ error: "Error al obtener voluntarios" });
    }
});

// POST /voluntario
router.post('/voluntario', async (req, res) => {
    try {
        const response = await axios.post(`${LOGISTICA_URL}/voluntario`, req.body);
        return res.status(201).json({ mensaje: response.data });
    } catch (error) {
        console.error("Error en Voluntario POST:", error.message);
        res.status(500).json({ error: "Error al registrar voluntario" });
    }
});

// PUT /voluntario/:rut/transporte/:transporteId
router.put('/voluntario/:rut/transporte/:transporteId', async (req, res) => {
    try {
        const { rut, transporteId } = req.params;
        const response = await axios.put(`${LOGISTICA_URL}/voluntario/${rut}/transporte/${transporteId}`);
        res.json(response.data);
    } catch (error) {
        console.error("Error asignando transporte a voluntario:", error.message);
        res.status(500).json({ error: "Error al asignar transporte al voluntario" });
    }
});

// PUT /voluntario/:rut/acopio/:acopioId
router.put('/voluntario/:rut/acopio/:acopioId', async (req, res) => {
    try {
        const { rut, acopioId } = req.params;
        const response = await axios.put(`${LOGISTICA_URL}/voluntario/${rut}/acopio/${acopioId}`);
        res.json(response.data);
    } catch (error) {
        console.error("Error asignando acopio a voluntario:", error.message);
        res.status(500).json({ error: "Error al asignar acopio al voluntario" });
    }
});

// GET /voluntario/transporte/:transporteId
router.get('/voluntario/transporte/:transporteId', async (req, res) => {
    try {
        const { transporteId } = req.params;
        const response = await axios.get(`${LOGISTICA_URL}/voluntario/transporte/${transporteId}`);
        res.json(response.data);
    } catch (error) {
        console.error("Error obteniendo voluntarios por transporte:", error.message);
        res.status(500).json({ error: "Error al obtener voluntarios por transporte" });
    }
});

// GET /voluntario/acopio/:acopioId
router.get('/voluntario/acopio/:acopioId', async (req, res) => {
    try {
        const { acopioId } = req.params;
        const response = await axios.get(`${LOGISTICA_URL}/voluntario/acopio/${acopioId}`);
        res.json(response.data);
    } catch (error) {
        console.error("Error obteniendo voluntarios por acopio:", error.message);
        res.status(500).json({ error: "Error al obtener voluntarios por acopio" });
    }
});

module.exports = router;
