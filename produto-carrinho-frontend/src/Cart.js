import React, { useContext, useState } from 'react';
import './Cart.css';
import { CartContext } from './contexts/CartContext';
import { toast } from 'react-toastify';
import { createOrder } from './services/orderService';

const Cart = () => {
    const { cartItems, removeFromCart, clearCart, addToCart, updateCartItemQuantity } = useContext(CartContext);
    const [isProcessing, setIsProcessing] = useState(false);

    const calcularSubtotal = () => {
        return cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    };

    const mapCartItemsForApi = () => {
        return cartItems.map(item => ({
            productId: item.id,
            quantity: item.quantity,
            price: item.price
        }));
    };

    const decrementItem = (itemId) => {
        const item = cartItems.find(item => item.id === itemId);

        if (item) {
            if (item.quantity > 1) {
                // Diminui a quantidade
                updateCartItemQuantity(itemId, item.quantity - 1);
            } else {
                // Se é o último, remove do carrinho
                removeFromCart(item);
            }
        }
    };

    const incrementItem = (itemId) => {
        const item = cartItems.find(item => item.id === itemId);

        if (item) {
            // Aumenta a quantidade
            updateCartItemQuantity(itemId, item.quantity + 1);
        }
    };

    const handleDirectRemove = (itemId) => {
        removeFromCart(itemId);

        toast.info('Produto removido do carrinho', {
            position: "top-right",
            autoClose: 3000
        });
    };

    const handleClearCart = () => {
        if (cartItems.length > 0) {
            clearCart();
            toast.info('Carrinho esvaziado com sucesso', {
                position: "top-right",
                autoClose: 3000
            });
        }
    };

    const handleCheckout = async () => {
        if (cartItems.length === 0) {
            toast.error('Seu carrinho está vazio. Adicione produtos antes de finalizar a compra.', {
                position: "top-right",
                autoClose: 5000
            });
            return;
        }

        setIsProcessing(true);

        try {
            const orderData = {
                items: mapCartItemsForApi(),
                totalAmount: calcularSubtotal()
            };

            const response = await createOrder(orderData);
            console.log('Resposta da API:', response);

            let orderId;
            if (response && typeof response === 'object') {
                orderId = response.id || response.orderId || response.orderNumber || response.number;

                if (!orderId && response.data) {
                    orderId = response.data.id || response.data.orderId || response.data.orderNumber || response.data.number;
                }
            }

            const orderText = orderId ? `Pedido #${orderId}` : 'Pedido';

            toast.success(`${orderText} realizado com sucesso!`, {
                position: "top-right",
                autoClose: 5000
            });
            clearCart();

        } catch (error) {
            console.error('Erro ao processar o pedido:', error);

            if (error.response && error.response.status === 409) {
                const unavailableItems = error.response.data.unavailableItems || [];

                if (unavailableItems.length > 0) {
                    const itemsList = unavailableItems.map(item =>
                        `${item.productName || 'Produto'} (${item.quantity} unidade(s))`
                    ).join(', ');

                    toast.error(`Alguns itens não estão disponíveis em estoque: ${itemsList}`, {
                        position: "top-right",
                        autoClose: 8000
                    });
                } else {
                    toast.error('Alguns itens não estão disponíveis em estoque.', {
                        position: "top-right",
                        autoClose: 5000
                    });
                }
            } else {
                toast.error('Não foi possível processar seu pedido. Tente novamente.', {
                    position: "top-right",
                    autoClose: 5000
                });
            }
        } finally {
            setIsProcessing(false);
        }
    };

    return (
        <div className="cart-container">
            <h2>Seu Carrinho</h2>

            {cartItems.length === 0 ? (
                <p>Seu carrinho está vazio</p>
            ) : (
                <>
                    <ul className="cart-items">
                        {cartItems.map(item => (
                            <li key={item.id} className="cart-item">
                                <div className="item-info">
                                    <img
                                        src={item.image || 'placeholder.jpg'}
                                        alt={`Imagem do produto ${item.name}`}
                                        className="item-image"
                                    />
                                    <div className="item-details">
                                        <h3>{item.name}</h3>
                                        <p>R$ {item.price.toFixed(2)}</p>
                                    </div>
                                </div>

                                <div className="item-quantity">
                                    <button
                                        onClick={() => decrementItem(item.id)}
                                        aria-label={`Diminuir quantidade de ${item.name}`}
                                        className="quantity-btn"
                                    >
                                        -
                                    </button>
                                    <span aria-live="polite" aria-label={`Quantidade: ${item.quantity}`}>
                                        {item.quantity}
                                    </span>
                                    <button
                                        onClick={() => incrementItem(item.id)}
                                        aria-label={`Aumentar quantidade de ${item.name}`}
                                        className="quantity-btn"
                                    >
                                        +
                                    </button>
                                </div>

                                <button
                                    onClick={() => handleDirectRemove(item.id)}
                                    aria-label={`Remover ${item.name} do carrinho`}
                                    className="remove-btn"
                                >
                                    X
                                </button>
                            </li>
                        ))}
                    </ul>

                    <div className="cart-summary">
                        <p>Subtotal: <strong>R$ {calcularSubtotal().toFixed(2)}</strong></p>
                        <button
                            onClick={handleCheckout}
                            disabled={isProcessing}
                            aria-label="Finalizar compra"
                            className="checkout-btn"
                        >
                            {isProcessing ? 'Processando...' : 'Finalizar Compra'}
                        </button>
                        <button
                            onClick={handleClearCart}
                            aria-label="Limpar carrinho"
                            className="clear-cart-btn"
                        >
                            Limpar Carrinho
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default Cart;