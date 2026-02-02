import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginUser } from "../Services/authService";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await loginUser(email, password);
      const { token, userId, role } = res.data;

      localStorage.setItem("token", token);
      localStorage.setItem("userId", userId);
      localStorage.setItem("role", role);

      if (role === "ADMIN") {
        navigate("/dashboard");
      } else {
        navigate("/jobs");
      }
    } catch (err) {
      alert("Invalid credentials");
    }
  };

  return (
    <div style={styles.page}>
      <form onSubmit={handleLogin} style={styles.card}>
        <h2 style={styles.title}>Sign in</h2>
        <p style={styles.subtitle}>Welcome back. Please login.</p>

        <label style={styles.label}>Email</label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com"
          required
          style={styles.input}
        />

        <label style={styles.label}>Password</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="••••••••"
          required
          style={styles.input}
        />

        <button type="submit" style={styles.button}>
          Login
        </button>

        <p style={styles.footer}>
          Don’t have an account?{" "}
          <Link to="/register" style={styles.link}>
            Register
          </Link>
        </p>
      </form>
    </div>
  );
}

/* ===== REAL-WORLD STYLES ===== */
const styles = {
  page: {
    minHeight: "100vh",
    background: "#f4f6f8",
    display: "flex",
    alignItems: "center",
    justifyContent: "center"
  },
  card: {
    width: "360px",
    background: "#ffffff",
    padding: "30px",
    borderRadius: "10px",
    boxShadow: "0 8px 20px rgba(0,0,0,0.08)"
  },
  title: {
    marginBottom: "6px",
    fontSize: "22px",
    textAlign: "center"
  },
  subtitle: {
    marginBottom: "20px",
    fontSize: "14px",
    color: "#666",
    textAlign: "center"
  },
  label: {
    fontSize: "13px",
    fontWeight: "600",
    marginBottom: "6px",
    display: "block"
  },
  input: {
    width: "100%",
    padding: "10px",
    marginBottom: "14px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "14px"
  },
  button: {
    width: "100%",
    padding: "10px",
    background: "#1976d2",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
    fontSize: "15px",
    fontWeight: "600",
    cursor: "pointer"
  },
  footer: {
    marginTop: "16px",
    fontSize: "13px",
    textAlign: "center"
  },
  link: {
    color: "#1976d2",
    fontWeight: "600",
    textDecoration: "none"
  }
};

export default Login;
