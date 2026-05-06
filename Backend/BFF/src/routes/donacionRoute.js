const express = require('express');
const axios = require('axios');

const router = express.Router();

// Endpoint: BFF consumiendo a tu microservicio en Java
router.get('/', async (req, res) => {
    try {
        console.log("Consultando microservicio de Java...");
        const response = await axios.get('http://localhost:8080/donacion');

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

module.exports = router; 