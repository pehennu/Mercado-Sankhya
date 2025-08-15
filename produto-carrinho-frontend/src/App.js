import React from 'react';
import ProductList from './ProductList';
import Cart from './Cart';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {CartProvider} from "./contexts/CartContext";

const App = () => {
    return (
        <CartProvider>
            <div style={{ display: 'flex' }}>
                <div style={{ width: '70%', padding: '1rem' }}>
                    <ProductList />
                </div>
                <div style={{ width: '30%', padding: '1rem', borderLeft: '1px solid #000' }}>
                    <Cart />
                </div>
            </div>
            <ToastContainer position="top-right" autoClose={3000} />
        </CartProvider>
    );
};

export default App;



