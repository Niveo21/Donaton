const express = require('express');
const axios = require('axios');

const router = express.Router();

router.use(express.json());

const NECESIDAD_URL = process.env.NECESIDAD_URL || 'http://localhost:8082';
const NOTIFICACION_URL = `${process.env.NOTIFICACION_URL || 'http://localhost:8083'}/notificacion`;

router.get('/listar', async (req, res) => {
    try {

        console.log("Consultando microservicio de Java...");
        const response = await axios.get(`${NECESIDAD_URL}/necesidad`);

        
        const dataOriginal = response.data;

        
        const dataLimpia = dataOriginal.map(item => ({
            nombre: item.nombre, 
            email: item.email,
            password: item.password,
            rol: item.rol      
        }));

        res.json(dataLimpia);
    } catch (error) {
        
        console.error("Error contactando microservicios:", error.message);
        res.status(500).json({ error: "Fallo de comunicación con Java" });
    }
});






router.post('/almacenar', async (req, res) => {
    try {
        const { acopioNombre, ...necesidad } = req.body;

        await axios.post(`${NECESIDAD_URL}/necesidad`, necesidad);

        const prefijo = necesidad.urgente ? 'URGENTE ' : '';
        const mensaje = `${prefijo}El punto "${acopioNombre || 'Sin nombre'}" reporta necesidad de ${necesidad.tipo}. ${necesidad.descripcion || ''}`.trim();

        await axios.post(NOTIFICACION_URL, { mensaje, leido: false });

        return res.status(201).json({ mensaje: 'Necesidad registrada y notificación enviada.' });
    } catch (error) {
        console.error("Error en el microservicio de necesidad:", error.message);
        res.status(500).json({ error: "No se pudo procesar la necesidad" });
    }
});

module.exports = router;

