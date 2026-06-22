const express = require('express');
const axios = require('axios');

const router = express.Router();

const DONACION_URL = process.env.DONACION_URL || 'http://localhost:8080';
const NOTIFICACION_URL = process.env.NOTIFICACION_URL || 'http://localhost:8083';

// Endpoint: BFF consumiendo a tu microservicio en Java
router.get('/', async (req, res) => {
    try {
        console.log("Consultando microservicio de Java...");
        const response = await axios.get(`${DONACION_URL}/donacion`);

        const dataOriginal = response.data;
        const dataLimpia = dataOriginal.map(item => ({
            recurso: item.tipoRecurso, 
            total: item.cantidad
              
        }));

        res.json(dataLimpia);
    } catch (error) {
        console.error("Error contactando microservicios:", error.message);
        res.status(500).json({ error: "Fallo de comunicación con Java" });
    }
});



router.post('/', async (req, res) => {
    try {
        const response = await axios.post(`${DONACION_URL}/donacion`, req.body);

        if (response.status === 200 || response.status === 201) {
            try {
                await axios.post(`${NOTIFICACION_URL}/notificacion`, {
                    mensaje: "¡Gracias! Tu donación ha sido recibida",
                    leido: false
                });
                console.log("Notificación enviada con éxito");
            } catch (errNoti) {
                // Si falla la notificación, solo lo logueamos para no arruinar la donación
                if (errNoti.response) {
                    console.error("Error en el microservicio de notificaciones:", errNoti.response.data);
                } else {
                    console.error("Error al notificar:", errNoti.message);
                }
            }

            // Siempre respondemos al cliente, independiente de si la notificación funcionó
            return res.status(201).json(response.data);
        }
    } catch (error) {
        console.error("Error en el microservicio de donaciones:", error.message);
        res.status(500).json({ error: "No se pudo procesar la donación" });
    }
});

module.exports = router; 