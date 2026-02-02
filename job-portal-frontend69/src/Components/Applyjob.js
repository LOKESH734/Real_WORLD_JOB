import React, { useState } from "react";
import { useParams } from "react-router-dom";

function ApplyJob() {
  const { jobId } = useParams();

  const [file, setFile] = useState(null);
  const [applied, setApplied] = useState(false);
  const [loading, setLoading] = useState(false);

  const userId = localStorage.getItem("userId");

  const applyJob = () => {
    if (applied || loading) return;

    setLoading(true);

    setTimeout(() => {
      setLoading(false);
      setApplied(true);

      const applications =
        JSON.parse(localStorage.getItem("applications")) || [];

      applications.push({
        id: Date.now(),
        userId,
        jobId,
        status: "PENDING"
      });

      localStorage.setItem("applications", JSON.stringify(applications));
    }, 2000);
  };

  return (
    <div style={styles.page}>
      <div style={styles.card}>
        <h2 style={styles.title}>Apply for this position</h2>
        <p style={styles.subtitle}>
          Upload your resume and submit your application
        </p>

        <label style={styles.label}>Resume</label>
        <input
          type="file"
          disabled={applied || loading}
          onChange={e => setFile(e.target.files[0])}
          style={styles.input}
        />

        <button
          onClick={applyJob}
          disabled={applied || loading}
          style={{
            ...styles.button,
            background: applied ? "#2e7d32" : "#1976d2",
            cursor: applied || loading ? "not-allowed" : "pointer"
          }}
        >
          {loading
            ? "Submitting Application..."
            : applied
            ? "Application Submitted ✅"
            : "Submit Application"}
        </button>

        {applied && (
          <p style={styles.success}>
            Thank you for applying. Our HR team will review your profile.
          </p>
        )}
      </div>
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
    width: "420px",
    background: "#ffffff",
    padding: "30px",
    borderRadius: "12px",
    boxShadow: "0 10px 25px rgba(0,0,0,0.08)"
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
    marginBottom: "18px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "14px"
  },
  button: {
    width: "100%",
    padding: "12px",
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    fontSize: "15px",
    fontWeight: "600"
  },
  success: {
    marginTop: "16px",
    fontSize: "13px",
    color: "#2e7d32",
    textAlign: "center"
  }
};

export default ApplyJob;
