const donacionRoute = require('./routes/donacionRoute');

app.use('/donacion', donacionRoute);


const registroRoute = require('./routes/registroRoute');

app.use('/register', registroRoute);

