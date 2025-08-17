import React, { useEffect, useState } from "react";
import axios from "axios";

function Dashboard({ token }) {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");
  const backendUrl = process.env.REACT_APP_BACKEND_URL;

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
    fetchDashboard();
  }, [token, backendUrl]);

  return (
    <div>
      <h2>Your Stored API Responses</h2>
      {error && <div style={{ color: "red" }}>{error}</div>}
      {items.length === 0 ? (
        <div>No APIs stored yet.</div>
      ) : (
        <ul>
          {items.map((item, idx) => (
            <li key={idx}>
              <strong>URL:</strong> {item.url}
              <br />
              <strong>Response:</strong>
              <pre style={{ background: "#eee", padding: "10px" }}>
                {item.jsonResponse}
              </pre>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default Dashboard;
