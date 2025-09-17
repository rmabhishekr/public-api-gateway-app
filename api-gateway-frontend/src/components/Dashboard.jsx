import React, { useEffect, useState } from "react";
import axios from "axios";
import InactivityLogoutTimer from "./InactivityLogoutTimer";
import { toast } from "react-toastify";

function Dashboard({ token, onLogout }) {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");
  const [apiUrl, setApiUrl] = useState("");
  const [loading, setLoading] = useState(false);
  const [openMenuIdx, setOpenMenuIdx] = useState(null);
  const [username, setUsername] = useState("");
  const displayName = username ? username.split(".")[0] : "";
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

  useEffect(() => {
    async function fetchUsername() {
      try {
        const res = await axios.get(`${backendUrl}/api/me`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        console.log("Fetched user:", res.data);
        setUsername(res.data.Username.toUpperCase());
      } catch {
        setUsername("");
      }
    }
    if (token) fetchUsername();
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
      toast.success("Success");
    } catch (err) {
      setError("Failed to submit API URL.");
      toast.error("Failed to submit API URL.");
    } finally {
      setLoading(false);
    }
  };

  const handleMenuClick = (id) => {
    setOpenMenuIdx(openMenuIdx === id ? null : id);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`${backendUrl}/api/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      // Remove the deleted item from state for instant UI update:
      setItems((prev) => prev.filter((item) => item.id !== id));
      setOpenMenuIdx(null);
    } catch (err) {
      setError("Failed to delete API URL.");
      setOpenMenuIdx(null);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6">
      <div className="max-w-4xl mx-auto bg-gray-800 rounded-xl p-6 shadow-lg">
        <div className="flex justify-between items-center mb-6">
          <div>
            <p className="text-gray-300 mt-1">
              {displayName && `Hey, ${displayName}`}
            </p>
          </div>
          <InactivityLogoutTimer onLogout={onLogout} />
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
            {items.map((item) => (
              <li
                key={item.id}
                className="bg-gray-700 rounded p-4 shadow-inner"
              >
                <div className="flex items-center justify-between">
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

                  {/* Three Dots (Kebab) Menu Button */}
                  <div className="relative">
                    <button
                      onClick={() => handleMenuClick(item.id)}
                      className="p-1 rounded-full hover:bg-gray-600 focus:outline-none"
                      aria-label="More options"
                      type="button"
                    >
                      <svg
                        width="20"
                        height="20"
                        fill="currentColor"
                        className="text-gray-300"
                      >
                        <circle cx="4" cy="10" r="2" />
                        <circle cx="10" cy="10" r="2" />
                        <circle cx="16" cy="10" r="2" />
                      </svg>
                    </button>
                    {/* Dropdown Menu */}
                    {openMenuIdx === item.id && (
                      <div className="absolute right-0 mt-2 w-28 bg-gray-800 rounded shadow-lg z-10">
                        <button
                          onClick={() => {
                            setOpenMenuIdx(null);
                            // Handle edit logic here
                            alert("Edit not implemented yet");
                          }}
                          className="w-full text-left px-4 py-2 hover:bg-gray-700 text-gray-200 rounded-t"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => {
                            console.log("Deleting id:", item.id, item);
                            handleDelete(item.id);
                          }}
                          className="w-full text-left px-4 py-2 hover:bg-gray-700 text-red-400 rounded-b"
                        >
                          Delete
                        </button>
                      </div>
                    )}
                  </div>
                </div>

                <p className="mt-2">
                  <strong>Response:</strong>
                </p>
                <pre className="mt-1 max-h-48 overflow-auto rounded bg-gray-800 p-3 text-xs font-mono text-green-300">
                  {item.responseJson}
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
