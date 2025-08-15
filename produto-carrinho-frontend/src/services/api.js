import axios from 'axios';

const apiUrl = process.env.REACT_APP_API_URL;

const api = axios.create({
    baseURL: apiUrl,
});

export const buscarProdutos = (search, pagina, tamanho) => {
    return api.get('/products', {
        params: {
            search,
            page: pagina,
            size: tamanho,
        },
    });
};

export const finalizarPedido = (payload) => {
    return api.post('/orders', payload);
};

export default api;
