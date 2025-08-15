import React, { createContext, useState } from 'react';

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cartItems, setCartItems] = useState([]);

    // Adicionar ou incrementar item no carrinho
    const addToCart = (produto) => {
        setCartItems((prev) => {
            const exists = prev.find(item => item.id === produto.id);
            if (exists) {
                return prev.map(item =>
                    item.id === produto.id ? { ...item, quantity: item.quantity + 1 } : item
                );
            }
            return [...prev, { ...produto, quantity: 1 }];
        });
    };

    // Remover ou decrementar item do carrinho
    const removeFromCart = (produto) => {
        setCartItems((prev) => {
            const exists = prev.find(item => item.id === produto.id);
            if (exists && exists.quantity > 1) {
                return prev.map(item =>
                    item.id === produto.id ? { ...item, quantity: item.quantity - 1 } : item
                );
            }
            return prev.filter(item => item.id !== produto.id);
        });
    };

    // Atualizar diretamente a quantidade de um item específico
    const updateCartItemQuantity = (itemId, newQuantity) => {
        setCartItems((prev) => {
            // Se a nova quantidade for 0 ou negativa, remove o item
            if (newQuantity <= 0) {
                return prev.filter(item => item.id !== itemId);
            }
            return prev.map(item =>
                item.id === itemId ? { ...item, quantity: newQuantity } : item
            );
        });
    };

    // Esvaziar carrinho
    const clearCart = () => {
        setCartItems([]);
    };

    return (
        <CartContext.Provider value={{
            cartItems,
            addToCart,
            removeFromCart,
            updateCartItemQuantity, // Adicionando a função ao Provider
            clearCart
        }}>
            {children}
        </CartContext.Provider>
    );
};