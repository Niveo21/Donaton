const axios = require('axios');


//Aqui debes instalar el axios npm install axios
const getUserData = async (userId) => {
    // Aquí podrías llamar a varios microservicios y combinar la info
    const response = await axios.get(`${process.env.AUTH_SERVICE_URL}/users/${userId}`);
    return response.data;
};

module.exports = { getUserData };