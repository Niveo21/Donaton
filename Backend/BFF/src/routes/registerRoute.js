const express = require('express');
const cors = require('cors');
const axios = require('axios'); 

const app = express();


app.use(cors());
app.use(express.json());

// Endpoint de ejemplo: BFF consumiendo a tu microservicio en Java
app.get('/', async (req, res) => {
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

app.listen(3000, () => console.log('BFF corriendo en puerto 3000'));


module.exports = { registroRoute };