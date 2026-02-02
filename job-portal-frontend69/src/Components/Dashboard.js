import React, { useEffect, useState } from "react";
import api from "../Services/api";

function Dashboard() {
  const role = localStorage.getItem("role");

  /* ================= USERS ================= */
  const [users, setUsers] = useState([]);
  const [keyword, setKeyword] = useState("");

  /* ================= JOBS ================= */
  const [jobs, setJobs] = useState([]);
  const [jobForm, setJobForm] = useState({
    title: "",
    description: "",
    company: "",
    location: ""
  });

  /* ================= APPLICATIONS ================= */
  const [applications, setApplications] = useState([]);

  /* ================= LOAD DATA ================= */
  useEffect(() => {
    if (role === "ADMIN") {
      fetchUsers();
      fetchJobs();
      fetchApplications();
    }
  }, [role]);

  /* ============== USERS ============== */
  const fetchUsers = async () => {
    const res = await api.get("/user/search?keyword=");
    setUsers(res.data);
  };

  const searchUsers = async () => {
    const res = await api.get(`/user/search?keyword=${keyword}`);
    setUsers(res.data);
  };

  const deleteUser = async (id) => {
    if (!window.confirm("Delete this user?")) return;
    await api.delete(`/user/delete/${id}`);
    setUsers(users.filter(u => u.id !== id));
  };

  /* ============== JOBS ============== */
  const fetchJobs = async () => {
    const res = await api.get("/jobs/all");
    setJobs(res.data);
  };

  const handleJobChange = (e) => {
    setJobForm({ ...jobForm, [e.target.name]: e.target.value });
  };

  const addJob = async (e) => {
    e.preventDefault();
    await api.post("/jobs/add", jobForm);
    setJobForm({ title: "", description: "", company: "", location: "" });
    fetchJobs();
  };

  const deleteJob = async (id) => {
    if (!window.confirm("Delete this job?")) return;
    await api.delete(`/jobs/delete/${id}`);
    setJobs(jobs.filter(j => j.id !== id));
  };

  /* ============== APPLICATIONS (ONLY THIS PART CHANGED) ============== */
  const fetchApplications = () => {
    const storedApplications =
      JSON.parse(localStorage.getItem("applications")) || [];

    setApplications(
      storedApplications.map(app => ({
        id: app.id,
        userId: app.userId,
        jobId: app.jobId,
        status: app.status || "PENDING"
      }))
    );
  };

  const updateStatus = (applicationId, status) => {
    const updated = applications.map(app =>
      app.id === applicationId
        ? { ...app, status }
        : app
    );

    setApplications(updated);
    localStorage.setItem("applications", JSON.stringify(updated));
  };

  if (role !== "ADMIN") {
    return <h2>Access Denied</h2>;
  }

  /* ================= UI ================= */
  const sectionStyle = {
    border: "1px solid #ddd",
    borderRadius: "8px",
    padding: "16px",
    marginBottom: "20px",
    background: "#fafafa"
  };

  const appCard = {
    border: "1px solid #e0e0e0",
    borderRadius: "10px",
    padding: "16px",
    marginBottom: "14px",
    background: "#fff",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center"
  };

  const statusBadge = (status) => ({
    padding: "6px 14px",
    borderRadius: "20px",
    fontSize: "13px",
    fontWeight: "bold",
    color: "#fff",
    background:
      status === "ACCEPTED"
        ? "#2e7d32"
        : status === "REJECTED"
        ? "#c62828"
        : "#ed6c02"
  });

  return (
    <div style={{ padding: "20px", maxWidth: "1100px", margin: "auto" }}>
      <h1 style={{ textAlign: "center" }}>ADMIN DASHBOARD</h1>

      {/* ================= USERS ================= */}
      <div style={sectionStyle}>
        <h2>Users</h2>
        <input
          placeholder="Search users"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button onClick={searchUsers}>Search</button>

        {users.map(u => (
          <div key={u.id} style={{ marginTop: "8px" }}>
            {u.name} ({u.email})
            <button
              style={{ marginLeft: "10px" }}
              onClick={() => deleteUser(u.id)}
            >
              Delete
            </button>
          </div>
        ))}
      </div>

      {/* ================= JOBS ================= */}
      <div style={sectionStyle}>
        <h2>Add Job</h2>
        <form onSubmit={addJob}>
          <input name="title" placeholder="Title" value={jobForm.title} onChange={handleJobChange} />
          <input name="company" placeholder="Company" value={jobForm.company} onChange={handleJobChange} />
          <input name="location" placeholder="Location" value={jobForm.location} onChange={handleJobChange} />
          <textarea name="description" placeholder="Description" value={jobForm.description} onChange={handleJobChange} />
          <br />
          <button type="submit">Add Job</button>
        </form>

        <h3 style={{ marginTop: "15px" }}>Jobs</h3>
        {jobs.map(job => (
          <div key={job.id}>
            {job.title} - {job.company}
            <button style={{ marginLeft: "10px" }} onClick={() => deleteJob(job.id)}>
              Delete
            </button>
          </div>
        ))}
      </div>

      {/* ================= APPLICATIONS ================= */}
      <div style={sectionStyle}>
        <h2>Applications</h2>

        {applications.length === 0 && <p>No applications found</p>}

        {applications.map(app => (
          <div key={app.id} style={appCard}>
            <div>
              <p><strong>User ID:</strong> {app.userId}</p>
              <p><strong>Job ID:</strong> {app.jobId}</p>
            </div>

            <div style={{ textAlign: "right" }}>
              <div style={statusBadge(app.status)}>
                {app.status}
              </div>

              <div style={{ marginTop: "10px" }}>
                <button
                  disabled={app.status !== "PENDING"}
                  onClick={() => updateStatus(app.id, "ACCEPTED")}
                  style={{ marginRight: "8px" }}
                >
                  Accept
                </button>

                <button
                  disabled={app.status !== "PENDING"}
                  onClick={() => updateStatus(app.id, "REJECTED")}
                >
                  Reject
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Dashboard;
