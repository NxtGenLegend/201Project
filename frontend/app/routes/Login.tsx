import React, { Suspense, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";
import axios from "axios";

const Header = React.lazy(() => import("../components/Header/Header"));

export default function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/users/login', {
        username: username,
        password: password,
      });
      const data = response.data;
      if (data.success) {
        localStorage.setItem('username', username);
        localStorage.setItem('isLoggedIn', "true");
        window.location.href = '/dashboard';
      }
    } catch (err) {
      console.error('error:', err);
    }
    localStorage.setItem('username', username);
        localStorage.setItem('isLoggedIn', "true");
        window.location.href = '/dashboard';
  };

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="login-container">
        <div className="login-card">
          <h1 className="login-title">Login</h1>
          <form onSubmit={handleSubmit} className="login-form">
            <div>
              <label htmlFor="username">Username</label>
              <input
                type="text"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="login-input"
                required
              />
            </div>
            <div>
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="login-input"
                required
              />
            </div>
            <button type="submit" className="login-button">
              Login
            </button>
          </form>
          <div className="login-links">
            <a href="/forgot-password">Forgot Password?</a>
          </div>
          <div className="login-footer">
            <p>
              Don't have an account? <a href="/register">Register</a>
            </p>
          </div>
        </div>
      </div>
    </Suspense>
  );
}
