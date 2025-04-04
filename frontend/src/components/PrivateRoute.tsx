import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  sub: string;
  role: string;
  exp: number;
}

interface PrivateRouteProps {
  adminOnly?: boolean;
}

const PrivateRoute: React.FC<PrivateRouteProps> = ({ adminOnly = false }) => {
  const token = localStorage.getItem('token');

  if (!token) {
    console.log("No token found, redirecting to login.");
    return <Navigate to="/login" replace />;
  }

  try {
    const decoded = jwtDecode(token) as JwtPayload;
    console.log("Decoded token:", decoded);
    if (adminOnly && decoded.role !== 'ROLE_ADMIN') {
      console.log("User is not an admin, redirecting to home.");
      return <Navigate to="/home" replace />;
    }
  } catch (error) {
    console.error("Error decoding token:", error);
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default PrivateRoute;
