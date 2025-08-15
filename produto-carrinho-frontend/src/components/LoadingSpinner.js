import React from 'react';
import './LoadingSpinner.css';

const LoadingSpinner = () => {
    return (
        <div className="spinner-container">
            <div className="spinner"></div>
            <p>Carregando produtos...</p>
        </div>
    );
};

export default LoadingSpinner;
