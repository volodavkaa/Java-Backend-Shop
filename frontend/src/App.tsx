import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import CreateProductPage from './pages/CreateProductPage';
import ProductListPage from './pages/ProductListPage';
import AdminPanel from './pages/AdminPanel';
import PrivateRoute from './components/PrivateRoute';

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        {/* Публічні маршрути */}
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Захищені маршрути для авторизованих користувачів */}
        <Route element={<PrivateRoute />}>
          <Route path="/home" element={<HomePage />} />
          <Route path="/products" element={<ProductListPage />} />
          <Route path="/products/create" element={<CreateProductPage />} />
        </Route>

        {/* Маршрути лише для адміністраторів */}
        <Route element={<PrivateRoute adminOnly={true} />}>
          <Route path="/admin" element={<AdminPanel />} />
        </Route>

        {}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
