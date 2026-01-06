import { Link, useLocation } from "react-router-dom";
import "../styles/layout.css";


function Navbar({ onLogout, userRole }) {
  const location = useLocation();
  const isSlotsPage = location.pathname === '/slots';

  return (
    <nav className={`navbar ${isSlotsPage ? 'navbar-dynamic' : ''}`}>
      <h3>Smart Parking</h3>
      <div className="nav-links">
        {userRole === "ADMIN" ? (
          <Link to="/admin">Admin Panel</Link>
        ) : userRole === "OWNER" ? (
          <Link to="/owner">Slot Maintenance</Link>
        ) : (
          <Link to="/dashboard">Dashboard</Link>
        )}
        <Link to="/slots">Available Slots</Link>
        {userRole !== "ADMIN" && <Link to="/bookings">My Bookings</Link>}
        <button onClick={onLogout}>Logout</button>
      </div>
    </nav>
  );
}

export default Navbar;
