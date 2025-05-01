import React, { Suspense, useState } from "react";
import "../styles/Register.css";

const Header = React.lazy(() => import("../components/Header/Header"));

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    // Handle registration logic here
    console.log("Email:", email);
    console.log("Password:", password);
  };

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="register-container">
        <div className="register-card">
          <h1 className="register-title">Register</h1>
          <form onSubmit={handleSubmit} className="register-form">
            <div>
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="register-input"
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
                className="register-input"
                required
              />
            </div>
            <div>
              <label htmlFor="confirm-password">Confirm Password</label>
              <input
                type="password"
                id="confirm-password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="register-input"
                required
              />
            </div>
            <button type="submit" className="register-button">
              Register
            </button>
          </form>
          <div className="register-footer">
            <p>
              Already have an account? <a href="/login">Login</a>
            </p>
          </div>
        </div>
      </div>
    </Suspense>
  );
}