import { useState } from "react";
import axios from "axios";
import "../styles/login.css";


function LoginPage({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async () => {
  setError("");
  try {
    const res = await axios.post(
      "http://localhost:8080/api/users/login",
      { email, password }
    );

    // ✅ Store token ONLY
    localStorage.setItem("token", res.data.token);

    // ✅ Store user info separately (used later)
    localStorage.setItem(
      "user",
      JSON.stringify({
        id: res.data.id,
        name: res.data.name,
        email: res.data.email,
        role: res.data.role,
        hasSlots: res.data.hasSlots
      })
    );

    onLogin();
  } catch {
    setError("Invalid email or password");
  }
};


  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Smart Parking Login</h2>

        {error && <p className="error">{error}</p>}

        <input
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />


        <button onClick={handleLogin}>Login</button>
        <p style={{ marginTop: 16 }}>
          Don't have an account? <a href="/signup">Sign up</a>
        </p>
      </div>
    </div>
  );
}

export default LoginPage;
