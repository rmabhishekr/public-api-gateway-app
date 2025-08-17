import React, { useState } from "react";
import SignIn from "./components/SignIn";
import SubmitApiUrl from "./components/SubmitApiUrl";
import Dashboard from "./components/Dashboard";
import SignupForm from "./components/SignupForm";

function App() {
  const [token, setToken] = useState(localStorage.getItem("jwtToken") || "");
  const [showSignup, setShowSignup] = useState(false);

  const handleSetToken = (tok) => {
    setToken(tok);
    localStorage.setItem("jwtToken", tok);
  };

  const handleLogout = () => {
    setToken("");
    localStorage.removeItem("jwtToken");
  };

  if (!token) {
    if (showSignup) {
      return (
        <div>
          <SignupForm backendUrl={process.env.REACT_APP_BACKEND_URL} />
          <button onClick={() => setShowSignup(false)}>
            Already have an account? Sign In
          </button>
        </div>
      );
    }
    return (
      <div>
        <SignIn setToken={handleSetToken} />
        <button onClick={() => setShowSignup(true)}>
          Don't have an account? Sign Up
        </button>
      </div>
    );
  }

  return (
    <div style={{ maxWidth: "650px", margin: "auto", padding: "20px" }}>
      <button onClick={handleLogout}>Logout</button>
      <SubmitApiUrl token={token} onSubmitSuccess={() => {}} />
      <Dashboard token={token} />
    </div>
  );
}
export default App;
