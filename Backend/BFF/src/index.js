const express = require('express');
const donacionRoute = require('./routes/donacionRoute');
const registerRoute = require('./routes/registerRoute');

const app = express();

app.use(express.json());

app.use('/donacion', donacionRoute);

app.use('/usuario', registerRoute);

app.use('/necesidad', registerRoute);// Descomentar cuando el archivo exista

app.listen(3000, () => console.log('BFF corriendo en puerto 3000'));

