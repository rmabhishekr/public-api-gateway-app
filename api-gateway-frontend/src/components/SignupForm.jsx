import React, { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

function SignupForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const backendUrl = import.meta.env.VITE_BACKEND_URL;

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
      await axios.post(
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
    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950 flex items-center justify-center p-4">
      <div className="w-full max-w-md rounded-2xl border border-white/10 bg-white/5 backdrop-blur-xl shadow-2xl p-8">
        <div className="mb-6 text-center">
          <h2 className="text-2xl font-bold tracking-tight text-white">
            Create an account
          </h2>
          <p className="mt-1 text-sm text-slate-300">
            Please fill in your details to sign up.
          </p>
        </div>

        {error && (
          <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-2 text-sm text-red-700">
            {error}
          </div>
        )}
        {success && (
          <div className="mb-4 rounded-lg border border-green-200 bg-green-50 px-4 py-2 text-sm text-green-700">
            {success}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label
              htmlFor="username"
              className="mb-1 block text-sm font-medium text-slate-200"
            >
              Username
            </label>
            <input
              id="username"
              type="text"
              placeholder="jane.doe"
              value={username}
              autoComplete="username"
              onChange={(e) => setUsername(e.target.value)}
              required
              className="block w-full rounded-xl border border-white/10 bg-white/10 px-4 py-3 text-white placeholder-slate-400 shadow-sm outline-none focus:border-indigo-400 focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <div>
            <label
              htmlFor="password"
              className="mb-1 block text-sm font-medium text-slate-200"
            >
              Password
            </label>
            <input
              id="password"
              type="password"
              placeholder="••••••••"
              value={password}
              autoComplete="new-password"
              onChange={(e) => setPassword(e.target.value)}
              required
              className="block w-full rounded-xl border border-white/10 bg-white/10 px-4 py-3 text-white placeholder-slate-400 shadow-sm outline-none focus:border-indigo-400 focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <button
            type="submit"
            className="mt-2 inline-flex w-full items-center justify-center rounded-xl bg-indigo-600 px-4 py-3 font-semibold text-white shadow-lg transition active:scale-[.99] hover:bg-indigo-500"
          >
            Sign Up
          </button>

          <p className="text-center text-sm text-slate-300">
            Already have an account?{" "}
            <Link
              to="/signin"
              className="font-medium text-indigo-400 hover:text-indigo-300"
            >
              Sign in
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}

export default SignupForm;
