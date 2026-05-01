import axios from "axios";

const api = axios.create({
  // ✅ FIXED: No localhost, use same domain (Ingress)
  baseURL: process.env.REACT_APP_API_URL || ""
});

// JWT
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auto logout
api.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) {
      localStorage.clear();
      window.location.href = "/login";
    }
    return Promise.reject(err);
  }
);

export default api;