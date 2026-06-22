const express = require('express');
const axios = require('axios');

const router = express.Router();

const CHAT_URL = 'http://localhost:8083/chat';

// GET /chat — Historial de mensajes del chat de voluntarios
router.get('/', async (req, res) => {
    try {
        const response = await axios.get(CHAT_URL);
        res.json(response.data);
    } catch (error) {
        console.error("Error al obtener historial de chat:", error.message);
        res.status(500).json({ error: "No se pudo obtener el historial del chat" });
    }
});

module.exports = router;
