import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1';

export const createOrder = async (orderData) => {
    try {
        const response = await axios.post(`${API_URL}/orders`, orderData);
        return response.data;
    } catch (error) {
        console.error('Erro ao criar pedido:', error);

        if (error.response && error.response.status === 409) {
            console.error('Conflito: Itens indisponíveis em estoque', error.response.data);
        }

        throw error;
    }
};

export const getUserOrders = async () => {
    try {
        const response = await axios.get(`${API_URL}/orders`);
        return response.data;
    } catch (error) {
        console.error('Erro ao buscar pedidos:', error);
        throw error;
    }
};

export const getOrderById = async (orderId) => {
    try {
        const response = await axios.get(`${API_URL}/orders/${orderId}`);
        return response.data;
    } catch (error) {
        console.error(`Erro ao buscar pedido ${orderId}:`, error);
        throw error;
    }
};
