const express = require('express');
const cors = require('cors');
const donacionRoute = require('./routes/donacionRoute');
const registerRoute = require('./routes/registerRoute');

const app = express();

app.use(cors());
app.use(express.json());

app.use('/donacion', donacionRoute);

app.use('/register', registerRoute); // Descomentar cuando el archivo exista

app.listen(3000, () => console.log('BFF corriendo en puerto 3000'));

