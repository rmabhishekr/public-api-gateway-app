import React, { useEffect, useState } from "react";
import axios from "axios";

function Dashboard({ token, onLogout }) {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");
  const [apiUrl, setApiUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  // Fetch stored APIs on mount or token change
  useEffect(() => {
    async function fetchDashboard() {
      setError("");
      try {
        const res = await axios.get(`${backendUrl}/api/dashboard`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setItems(res.data);
      } catch (err) {
        setError("Failed to fetch dashboard.");
        setItems([]);
      }
    }
    if (token) fetchDashboard();
  }, [token, backendUrl]);

  // Submit new public API URL
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!apiUrl.trim()) return;
    setLoading(true);
    setError("");
    try {
      await axios.post(
        `${backendUrl}/api/add`,
        { url: apiUrl },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setApiUrl("");
      // Refresh list after submission
      const res = await axios.get(`${backendUrl}/api/dashboard`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setItems(res.data);
    } catch (err) {
      setError("Failed to submit API URL.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="max-w-4xl mx-auto bg-gray-800 rounded-xl p-6 shadow-lg">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-3xl font-semibold">Your Stored API Responses</h2>
          <button
            onClick={onLogout}
            className="px-4 py-2 bg-red-600 hover:bg-red-700 rounded font-semibold transition"
          >
            Logout
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex gap-4 mb-6">
          <input
            type="url"
            placeholder="Enter public API URL"
            value={apiUrl}
            onChange={(e) => setApiUrl(e.target.value)}
            className="flex-grow px-4 py-2 rounded border border-gray-600 bg-gray-700 placeholder-gray-400 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
          <button
            type="submit"
            disabled={loading}
            className="px-6 py-2 bg-blue-600 hover:bg-blue-700 rounded font-semibold disabled:opacity-50 disabled:cursor-not-allowed transition"
          >
            {loading ? "Submitting..." : "Submit"}
          </button>
        </form>

        {error && (
          <div className="mb-6 p-3 bg-red-700 rounded text-center text-red-100 font-medium">
            {error}
          </div>
        )}

        {items.length === 0 ? (
          <div className="text-gray-400 text-center">No APIs stored yet.</div>
        ) : (
          <ul className="space-y-6 max-h-[400px] overflow-y-auto">
            {items.map((item, idx) => (
              <li key={idx} className="bg-gray-700 rounded p-4 shadow-inner">
                <p>
                  <strong>URL:</strong>{" "}
                  <a
                    href={item.url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="text-blue-400 hover:underline"
                  >
                    {item.url}
                  </a>
                </p>
                <p className="mt-2">
                  <strong>Response:</strong>
                </p>
                <pre className="mt-1 max-h-48 overflow-auto rounded bg-gray-800 p-3 text-xs font-mono text-green-300">
                  {item.jsonResponse}
                </pre>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
