// src/Components/JobList.js
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../Services/api";

function JobList() {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        const res = await api.get("/jobs/all");

        if (!res.data || res.data.length === 0) {
          setError("No jobs found");
          setJobs([]);
        } else {
          setJobs(res.data);
        }
      } catch (err) {
        if (!err.response) {
          setError("Backend not reachable");
        } else if (err.response.status === 401) {
          setError("Unauthorized – login again");
        } else {
          setError("Failed to load jobs");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <h1>Available Jobs</h1>

      {loading && <p>Loading jobs...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {!loading && !error && jobs.map(job => (
        <div
          key={job.id}
          style={{
            border: "1px solid #ccc",
            padding: "15px",
            marginBottom: "12px",
            cursor: "pointer"
          }}
          onClick={() => navigate(`/apply/${job.id}`)}   // ✅ CLICK JOB
        >
          <h3>{job.title}</h3>
          <p>{job.company} – {job.location}</p>
          <p>{job.description}</p>

          <button
            onClick={(e) => {
              e.stopPropagation(); // 🛑 prevent double click
              navigate(`/apply/${job.id}`);
            }}
          >
            Apply
          </button>
        </div>
      ))}
    </div>
  );
}

export default JobList;
