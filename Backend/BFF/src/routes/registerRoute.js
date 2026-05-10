const express = require('express');
const cors = require('cors');
const axios = require('axios'); 

const router = express.Router();


router.use(cors());
router.use(express.json());


router.get('/', async (req, res) => {
    try {
       
        console.log("Consultando microservicio de Java...");
        const response = await axios.get('http://localhost:8084/registro');

        
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




module.exports = router;

router.post('/', async (req, res) => {
    try {
        const response = await axios.post('http://localhost:8084/registro', req.body);
        
        
        return res.status(201).json(response.data);


    }catch (error) {
        console.error("Error en el microservicio de registro:", error.message);
        res.status(500).json({ error: "No se pudo procesar el registro" });
    }
});