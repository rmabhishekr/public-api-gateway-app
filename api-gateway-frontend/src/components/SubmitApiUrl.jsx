import React, { useState } from "react";
import axios from "axios";

function SubmitApiUrl({ token, onSubmitSuccess }) {
  const [url, setUrl] = useState("");
  const [response, setResponse] = useState("");
  const [error, setError] = useState("");
  const backendUrl = import.meta.env.VITE_BACKEND_URL;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await axios.post(
        `${backendUrl}/api/add`,
        { url },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setResponse(JSON.stringify(res.data, null, 2));
      setUrl("");
      onSubmitSuccess && onSubmitSuccess(); // refresh dashboard after add
    } catch (err) {
      setError(
        err.res?.data?.error ||
          err.res?.data?.message ||
          err.message ||
          "Failed to submit. Check URL or token."
      );
    }
  };

  return (
    <div>
      <h2>Add Public API URL</h2>
      {error && <div style={{ color: "red" }}>{error}</div>}
      <form onSubmit={handleSubmit}>
        <input
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          placeholder="Public JSON API URL"
          required
        />
        <button type="submit">Submit</button>
      </form>
      {response && (
        <div>
          <h3>API Response:</h3>
          <pre style={{ background: "#eee", padding: "10px" }}>{response}</pre>
        </div>
      )}
    </div>
  );
}

export default SubmitApiUrl;
