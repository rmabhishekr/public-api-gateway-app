import React, { useState } from "react";
import axios from "axios";

function SignupForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

  function validatePassword(pw) {
    // At least 8 chars, upper and lowercase, digit, special char
    const re =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
    return re.test(pw);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!validatePassword(password)) {
      setError(
        "Password must be at least 8 characters, include upper and lower case, a digit, and a special character."
      );
      return;
    }

    try {
      const res = await axios.post(
        `${backendUrl}/auth/signup`,
        { username, password },
        { headers: { "Content-Type": "application/json" } }
      );
      setSuccess("Registration successful! You can now log in.");
      setUsername("");
      setPassword("");
    } catch (err) {
      if (err.response && err.response.data) {
        setError(
          err.response.data.message ||
            err.response.data.error ||
            "Signup failed. Try a different username."
        );
      } else {
        setError("Network or server error.");
      }
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "auto", padding: 20 }}>
      <h2>Sign Up</h2>
      {error && <div style={{ color: "red", marginBottom: 10 }}>{error}</div>}
      {success && (
        <div style={{ color: "green", marginBottom: 10 }}>{success}</div>
      )}
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 10 }}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            autoComplete="username"
            onChange={(e) => setUsername(e.target.value)}
            required
            style={{ width: "100%", padding: 8 }}
          />
        </div>
        <div style={{ marginBottom: 10 }}>
          <input
            type="password"
            placeholder="Password"
            value={password}
            autoComplete="new-password"
            onChange={(e) => setPassword(e.target.value)}
            required
            style={{ width: "100%", padding: 8 }}
          />
        </div>
        <button type="submit" style={{ width: "100%", padding: 8 }}>
          Register
        </button>
      </form>
    </div>
  );
}

export default SignupForm;
