import React, { useState, useEffect, useCallback, useContext } from 'react';
import debounce from 'lodash.debounce';
import { buscarProdutos } from './services/api';
import { CartContext } from './contexts/CartContext';
import LoadingSpinner from './components/LoadingSpinner';
import { toast } from 'react-toastify';
import './ProductList.css';

const ProductList = () => {
    const [produtos, setProdutos] = useState([]);
    const [search, setSearch] = useState('');
    const [pagina, setPagina] = useState(0);
    const [tamanho, setTamanho] = useState(6);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(false);

    const { addToCart } = useContext(CartContext);

    const fetchProdutos = useCallback(
        debounce(async (searchTerm, page, size) => {
            setLoading(true);
            try {
                const response = await buscarProdutos(searchTerm, page, size);

                if (response?.data?.success && response.data.data) {
                    const apiData = response.data.data;

                    if (Array.isArray(apiData.content)) {
                        setProdutos(apiData.content);
                        setTotalPages(apiData.totalPages || 0);
                    } else {
                        setProdutos([]);
                        console.warn("Campo 'content' não é um array:", apiData);
                    }
                } else {
                    setProdutos([]);
                    console.warn("Resposta da API não está no formato esperado:", response);
                }
            } catch (error) {
                console.error("Erro ao buscar produtos:", error);
                setProdutos([]);
                setTotalPages(0);
                toast.error("Erro ao buscar produtos, tente novamente.");
            } finally {
                setLoading(false);
            }
        }, 500),
        []
    );


    const handleSearchChange = (e) => {
        setSearch(e.target.value);
        setPagina(0);
    };

    const handleAddToCart = (produto) => {
        addToCart(produto);

        const el = document.createElement('div');
        el.className = 'cart-animation';
        el.textContent = '✓';
        document.body.appendChild(el);

        setTimeout(() => {
            document.body.removeChild(el);
        }, 1000);

        toast.success(`${produto.name} adicionado ao carrinho!`);
    };

    useEffect(() => {
        fetchProdutos(search, pagina, tamanho);
    }, [search, pagina, tamanho, fetchProdutos]);

    return (
        <div className="product-container">
            <h2 className="product-title">Nossos Produtos</h2>

            <div className="search-container">
                <input
                    type="text"
                    className="search-input"
                    aria-label="Busca de produtos"
                    placeholder="O que você está procurando hoje?"
                    value={search}
                    onChange={handleSearchChange}
                />
                <i className="search-icon">🔍</i>
            </div>

            {loading ? (
                <div className="loading-container">
                    <LoadingSpinner />
                </div>
            ) : (
                <>
                    {Array.isArray(produtos) && produtos.length > 0 ? (
                        <div className="product-grid">
                            {produtos.map(produto => (
                                <div key={produto.id} className="product-card">
                                    <div className="product-image">
                                        {produto.imageUrl ? (
                                            <img src={produto.imageUrl} alt={produto.name} />
                                        ) : (
                                            <div className="placeholder-image">📦</div>
                                        )}
                                    </div>
                                    <div className="product-info">
                                        <h3 className="product-name">{produto.name}</h3>
                                        <p className="product-price">R$ {produto.price.toFixed(2)}</p>
                                        <button
                                            className="add-to-cart-btn"
                                            aria-label={`Adicionar ${produto.name} ao carrinho`}
                                            onClick={() => handleAddToCart(produto)}
                                        >
                                            Adicionar ao Carrinho <span>+</span>
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="no-products">
                            <p>Nenhum produto encontrado.</p>
                            {search && (
                                <p className="search-suggestion">
                                    Tente buscar usando palavras-chave diferentes ou navegue por todas as categorias.
                                </p>
                            )}
                        </div>
                    )}

                    {totalPages > 1 && (
                        <div className="pagination">
                            <button
                                className={`pagination-btn ${pagina === 0 ? 'disabled' : ''}`}
                                disabled={pagina === 0}
                                onClick={() => setPagina(pagina - 1)}
                            >
                                &laquo; Anterior
                            </button>

                            <div className="page-numbers">
                                {[...Array(Math.min(5, totalPages)).keys()].map(i => {
                                    const pageToShow = Math.min(
                                        Math.max(pagina - 2 + i, 0),
                                        totalPages - 1
                                    );
                                    return (
                                        <button
                                            key={pageToShow}
                                            className={`page-number ${pageToShow === pagina ? 'active' : ''}`}
                                            onClick={() => setPagina(pageToShow)}
                                        >
                                            {pageToShow + 1}
                                        </button>
                                    );
                                })}
                            </div>

                            <button
                                className={`pagination-btn ${pagina >= totalPages - 1 ? 'disabled' : ''}`}
                                disabled={pagina >= totalPages - 1}
                                onClick={() => setPagina(pagina + 1)}
                            >
                                Próximo &raquo;
                            </button>
                        </div>
                    )}
                </>
            )}
        </div>
    );
};

export default ProductList;
