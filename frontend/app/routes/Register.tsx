import React, { Suspense, useState } from "react";
import "../styles/Register.css";
import axios from "axios";

const Header = React.lazy(() => import("../components/Header/Header"));

export default function Register() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/users/register', {
        first_name: firstName,
        last_name: lastName,
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
  };

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <Header />
      <div className="register-container">
        <div className="register-card">
          <h1 className="register-title">Register</h1>
          <form onSubmit={handleSubmit} className="register-form">
            <div>
              <label htmlFor="firstName">First Name</label>
              <input
                type="text"
                id="firstName"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                className="register-input"
                required
              />
            </div>
            <div>
              <label htmlFor="lastName">Last Name</label>
              <input
                type="text"
                id="lastName"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                className="register-input"
                required
              />
            </div>
            <div>
              <label htmlFor="username">Username</label>
              <input
                type="text"
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
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