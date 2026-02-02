import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerUser } from "../Services/authService";

function Register() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "USER",
    bio: "",
    profilePicUrl: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      await registerUser(formData);
      alert("Registration successful!");
      navigate("/login");
    } catch (error) {
      console.error(error);
      alert("Error during registration");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2 className="login-title">Create Account</h2>
        <p className="login-subtitle">Join us and get started today</p>

        <form onSubmit={handleRegister}>
          <input
            name="name"
            placeholder="Full Name"
            value={formData.name}
            onChange={handleChange}
            required
          />

          <input
            name="email"
            type="email"
            placeholder="Email Address"
            value={formData.email}
            onChange={handleChange}
            required
          />

          <input
            name="password"
            type="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <input
            name="role"
            placeholder="Role (USER / ADMIN)"
            value={formData.role}
            onChange={handleChange}
          />

          <input
            name="bio"
            placeholder="Short Bio"
            value={formData.bio}
            onChange={handleChange}
          />

          <input
            name="profilePicUrl"
            placeholder="Profile Picture URL"
            value={formData.profilePicUrl}
            onChange={handleChange}
          />

          <button type="submit">Register</button>
        </form>

        <p className="signup-text">
          Already have an account?{" "}
          <Link to="/login">Login here</Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
