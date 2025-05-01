import React, { Suspense, useState } from "react";
import "../styles/Login.css";

const Header = React.lazy(() => import("../components/Header/Header"));

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Handle login logic here
    console.log("Email:", email);
    console.log("Password:", password);
  };

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="login-container">
        <div className="login-card">
          <h1 className="login-title">Login</h1>
          <form onSubmit={handleSubmit} className="login-form">
            <div>
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
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
