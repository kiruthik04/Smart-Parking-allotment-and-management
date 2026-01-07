import { Link, useLocation } from "react-router-dom";
import { useState } from "react";
import "../styles/layout.css";


function Navbar({ onLogout, userRole }) {
  const location = useLocation();
  const isSlotsPage = location.pathname === '/slots';
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);
  const closeMenu = () => setIsMenuOpen(false);

  return (
    <nav className={`navbar ${isSlotsPage ? 'navbar-dynamic' : ''}`}>
      <h3>Smart Parking</h3>

      <button className="mobile-menu-btn" onClick={toggleMenu} aria-label="Toggle Menu">
        <span className={`hamburger ${isMenuOpen ? 'open' : ''}`}></span>
      </button>

      <div className={`nav-links ${isMenuOpen ? 'active' : ''}`}>
        {userRole === "ADMIN" ? (
          <Link to="/admin" onClick={closeMenu}>Admin Panel</Link>
        ) : userRole === "OWNER" ? (
          <Link to="/owner" onClick={closeMenu}>Slot Maintenance</Link>
        ) : (
          <Link to="/dashboard" onClick={closeMenu}>Dashboard</Link>
        )}
        <Link to="/slots" onClick={closeMenu}>Available Slots</Link>
        {userRole !== "ADMIN" && <Link to="/bookings" onClick={closeMenu}>My Bookings</Link>}
        <button className="logout-btn" onClick={() => { onLogout(); closeMenu(); }}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;
