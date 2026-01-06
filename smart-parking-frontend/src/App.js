import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useState, useEffect } from "react";

import Navbar from "./components/Navbar";
import SlotPage from "./pages/SlotsPage";
import BookingsPage from "./pages/BookingsPage";
import Dashboard from "./pages/Dashboard";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import OwnerDashboard from "./pages/OwnerDashboard";
import AdminDashboard from "./pages/AdminDashboard";
import ThemeToggle from "./components/ThemeToggle"; // Import

function App() {

  // Track auth state
  const [isAuthenticated, setIsAuthenticated] = useState(
    !!localStorage.getItem("token")
  );

  // Called after successful login
  const handleLogin = () => {
    setIsAuthenticated(true);
  };

  // Logout logic
  const handleLogout = () => {
    localStorage.removeItem("token");
    setIsAuthenticated(false);
  };

  // Determine user role for dashboard routing
  let userRole = null;
  try {
    const user = JSON.parse(localStorage.getItem("user"));
    userRole = user?.role;
  } catch { }

  // Auto Logout Logic (10 minutes inactivity)
  useEffect(() => {
    if (!isAuthenticated) return;

    let timeoutId;

    const resetTimer = () => {
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => {
        console.log("Auto-logging out due to inactivity");
        handleLogout();
        window.location.href = "/login"; // Force redirect
      }, 10 * 60 * 1000); // 10 minutes
    };

    const handleActivity = () => {
      resetTimer();
    };

    // Listeners for activity
    window.addEventListener("mousemove", handleActivity);
    window.addEventListener("keydown", handleActivity);
    window.addEventListener("click", handleActivity);
    window.addEventListener("scroll", handleActivity);

    // Initialize timer
    resetTimer();

    // Cleanup
    return () => {
      clearTimeout(timeoutId);
      window.removeEventListener("mousemove", handleActivity);
      window.removeEventListener("keydown", handleActivity);
      window.removeEventListener("click", handleActivity);
      window.removeEventListener("scroll", handleActivity);
    };
  }, [isAuthenticated]);

  return (
    <BrowserRouter>
      {isAuthenticated && <Navbar onLogout={handleLogout} userRole={userRole} />}
      <ThemeToggle /> {/* Add Global Toggle here */}
      <Routes>
        {/* LOGIN ROUTE */}
        <Route
          path="/login"
          element={
            isAuthenticated
              ? <Navigate to={userRole === "ADMIN" ? "/admin" : userRole === "OWNER" ? "/owner" : "/dashboard"} />
              : <LoginPage onLogin={handleLogin} />
          }
        />
        {/* SIGNUP ROUTE */}
        <Route
          path="/signup"
          element={isAuthenticated ? <Navigate to="/dashboard" /> : <SignupPage onSignup={() => window.location.replace('/login')} />}
        />
        {/* PROTECTED ROUTES */}
        <Route
          path="/slots"
          element={isAuthenticated ? <SlotPage /> : <Navigate to="/login" />}
        />
        <Route
          path="/dashboard"
          element={isAuthenticated && userRole !== "OWNER" && userRole !== "ADMIN" ? <Dashboard /> : <Navigate to={isAuthenticated ? (userRole === "ADMIN" ? "/admin" : "/owner") : "/login"} />}
        />
        <Route
          path="/owner"
          element={isAuthenticated && userRole === "OWNER" ? <OwnerDashboard /> : <Navigate to={isAuthenticated ? "/dashboard" : "/login"} />}
        />
        <Route
          path="/admin"
          element={isAuthenticated && userRole === "ADMIN" ? <AdminDashboard /> : <Navigate to="/login" />}
        />
        <Route
          path="/bookings"
          element={isAuthenticated ? <BookingsPage /> : <Navigate to="/login" />}
        />
        {/* DEFAULT ROUTE */}
        <Route
          path="*"
          element={<Navigate to={isAuthenticated ? (userRole === "ADMIN" ? "/admin" : userRole === "OWNER" ? "/owner" : "/slots") : "/login"} />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
