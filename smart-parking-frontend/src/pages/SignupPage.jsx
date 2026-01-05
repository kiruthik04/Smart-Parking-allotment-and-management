
import { useState } from "react";
import { signup } from "../services/api";
import "../styles/login.css";

function SignupPage({ onSignup }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("USER");
  const [error, setError] = useState("");
  const handleSignup = async () => {
    setError("");
    try {
      const data = { name, email, password, role };
      await signup(data);
      onSignup();
    } catch {
      setError("Signup failed. Try a different email.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Sign Up</h2>
        {error && <p className="error">{error}</p>}
        <input
          placeholder="Name"
          value={name}
          onChange={e => setName(e.target.value)}
        />
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
        <select value={role} onChange={e => setRole(e.target.value)}>
          <option value="USER">Normal User</option>
          <option value="OWNER">Owner</option>
        </select>

        <button onClick={handleSignup}>Sign Up</button>
      </div>
    </div>
  );
}

export default SignupPage;
