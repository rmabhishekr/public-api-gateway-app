import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const INACTIVITY_LIMIT = 15 * 60 * 1000;

export default function InactivityLogoutTimer({ onLogout }) {
  const navigate = useNavigate();

  useEffect(() => {
    let timer;

    // Events to monitor for user activity
    const events = [
      "mousemove",
      "mousedown",
      "keydown",
      "scroll",
      "touchstart",
    ];
    // Reset timer on activity
    const resetTimer = () => {
      clearTimeout(timer);
      timer = setTimeout(logout, INACTIVITY_LIMIT);
    };

    // Logout function
    const logout = () => {
      onLogout(); // Perform your logout logic (e.g. clear token, update state)
      navigate("/login"); // Redirect to login page
    };

    // Setup: monitor all activity events
    events.forEach((event) => window.addEventListener(event, resetTimer));
    resetTimer(); // Start the timer

    // Cleanup
    return () => {
      clearTimeout(timer);
      events.forEach((event) => window.removeEventListener(event, resetTimer));
    };
  }, [onLogout, navigate]);

  return null;
}
