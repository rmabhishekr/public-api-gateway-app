import React, { useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import SignIn from "./components/SignIn";
import SubmitApiUrl from "./components/SubmitApiUrl";
import Dashboard from "./components/Dashboard";
import SignupForm from "./components/SignupForm";

function App() {
  const [token, setToken] = useState(localStorage.getItem("jwtToken") || "");

  const handleSetToken = (tok) => {
    setToken(tok);
    localStorage.setItem("jwtToken", tok);
  };

  const handleLogout = () => {
    setToken("");
    localStorage.removeItem("jwtToken");
  };

  // Protect dashboard route
  const ProtectedRoute = ({ children }) => {
    if (!token) {
      return <Navigate to="/signin" replace />;
    }
    return children;
  };

  return (
    <Routes>
      <Route path="/signin" element={<SignIn setToken={handleSetToken} />} />
      <Route path="/signup" element={<SignupForm />} />
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <Dashboard token={token} onLogout={handleLogout} />
          </ProtectedRoute>
        }
      />
      {/* Redirect all other routes to Sign In if not authenticated, else Dashboard */}
      <Route
        path="*"
        element={<Navigate to={token ? "/dashboard" : "/signin"} replace />}
      />
    </Routes>
  );
}
export default App;
