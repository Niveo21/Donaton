const express = require('express');
const donacionRoute = require('./routes/donacionRoute');
const registerRoute = require('./routes/registerRoute');
const logisticaRoute = require('./routes/logisticaRoute');
const necesidadRoute = require('./routes/necesidadRoute');

const app = express();

app.use(express.json());

app.use('/donacion', donacionRoute);

app.use('/usuario', registerRoute);

app.use('/necesidad', necesidadRoute);// Descomentar cuando el archivo exista

app.use('/logistica', logisticaRoute);

app.listen(3000, () => console.log('BFF corriendo en puerto 3000'));

