import React, { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

function SignIn({ setToken }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await axios.post(`${backendUrl}/auth/login`, {
        username,
        password,
      });
      setToken(res.data.token);
    } catch (err) {
      setError("Invalid credentials or server error.");
    }
  };
  return (
    <div>
      <h2>Sign In</h2>
      {error && <div style={{ color: "red" }}>{error}</div>}
      <form onSubmit={handleSubmit}>
        <input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Username"
          required
        />
        <input
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
          placeholder="Password"
          required
        />
        <button type="submit">Sign In</button>
        <Link to="/signup">Don't have an account? Sign Up</Link>
      </form>
    </div>
  );
}
export default SignIn;
