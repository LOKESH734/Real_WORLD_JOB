import React, { useState, useEffect, useCallback, useMemo } from "react";
import api from "../Services/api"; // 🔁 CHANGED

const Application = () => {
  const [jobs, setJobs] = useState([]);
  const [jobId, setJobId] = useState("");
  const [title, setTitle] = useState("");
  const [company, setCompany] = useState("");
  const [location, setLocation] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const token = localStorage.getItem("token");

  const headers = useMemo(() => ({
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`
  }), [token]);

  // 🔁 CHANGED: use API_BASE_URL
  const fetchAllJobs = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      const response = await fetch(`${API_BASE_URL}/jobs/all`, { headers });
      if (!response.ok) {
        setError(`HTTP ${response.status}`);
        setJobs([]);
        return;
      }
      const data = await response.json();
      setJobs(data || []);
      setTotalPages(0);
    } catch (err) {
      setError(`Network error: ${err.message}`);
      setJobs([]);
    } finally {
      setLoading(false);
    }
  }, [headers]);

  const fetchJobs = useCallback(async () => {
    setLoading(true);
    setError("");
    try {
      if (jobId) {
        const response = await fetch(
          `${API_BASE_URL}/jobs/${jobId}`, // 🔁 CHANGED
          { headers }
        );
        if (!response.ok) {
          setError(`HTTP ${response.status}`);
          setJobs([]);
          return;
        }
        const data = await response.json();
        setJobs([data]);
        setTotalPages(0);
        return;
      }

      const params = new URLSearchParams({
        title: title || "",
        company: company || "",
        location: location || "",
        page: page,
        size: 5
      });

      const response = await fetch(
        `${API_BASE_URL}/jobs/search?${params}`, // 🔁 CHANGED
        { headers }
      );

      if (!response.ok) {
        setError(`HTTP ${response.status}`);
        setJobs([]);
        return;
      }

      const data = await response.json();
      setJobs(data.content || []);
      setTotalPages(data.totalPages || 0);

    } catch (err) {
      setError(`Network error: ${err.message}`);
      setJobs([]);
    } finally {
      setLoading(false);
    }
  }, [jobId, title, company, location, page, headers]);

  useEffect(() => {
    if (!jobId && !title && !company && !location) {
      fetchAllJobs();
    } else {
      fetchJobs();
    }
  }, [fetchAllJobs, fetchJobs, jobId, title, company, location, page]);

  return (
    <div style={{ padding: "20px" }}>
      <h1>Job Search</h1>

      <div style={{ marginBottom: "20px" }}>
        <input placeholder="Job ID" value={jobId} onChange={(e) => setJobId(e.target.value)} />
        <input placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} />
        <input placeholder="Company" value={company} onChange={(e) => setCompany(e.target.value)} />
        <input placeholder="Location" value={location} onChange={(e) => setLocation(e.target.value)} />
        <button onClick={() => { setPage(0); fetchJobs(); }}>
          Search
        </button>
      </div>

      {error && <p style={{ color: "red" }}>{error}</p>}

      {loading ? (
        <p>Loading jobs...</p>
      ) : jobs.length > 0 ? (
        <ul>
          {jobs.map(job => (
            <li key={job.id}>
              <strong>{job.title}</strong> — {job.company} ({job.location})
              <p>{job.description}</p>
            </li>
          ))}
        </ul>
      ) : (
        <p>No jobs found</p>
      )}

      {totalPages > 1 && (
        <div>
          <button disabled={page === 0} onClick={() => setPage(page - 1)}>
            Previous
          </button>
          <span> Page {page + 1} of {totalPages} </span>
          <button
            disabled={page === totalPages - 1}
            onClick={() => setPage(page + 1)}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default Application;
