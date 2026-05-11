const express = require('express');
const cors = require('cors');

const donacionRoute = require('./routes/donacionRoute');
const registerRoute = require('./routes/registerRoute');
const necesidadRoute = require('./routes/necesidadRoute');
const notificacionRoute = require('./routes/notificacionRoute');
const logisticaRoute = require('./routes/logisticaRoute');

const app = express();

app.use(cors());
app.use(express.json());

// Rutas del BFF 
app.use('/donacion', donacionRoute);      // puerto 8080
app.use('/usuario', registerRoute);      // puerto 8084
app.use('/necesidad', necesidadRoute);     // puerto 8082
app.use('/notificacion', notificacionRoute); // puerto 8083
app.use('/logistica', logisticaRoute);     // puerto 8081 (acopio, envio, inventario, transporte, voluntario)
//--

const PORT = 3000;
app.listen(PORT, () => console.log(`BFF corriendo en puerto ${PORT}`));
