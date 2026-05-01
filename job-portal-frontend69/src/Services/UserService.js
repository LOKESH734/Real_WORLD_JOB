import axios from "axios";

const API_URL = process.env.REACT_APP_API_URL || "";

// Helper to get token
const getAuthToken = () => localStorage.getItem("token");

// Search users
export const searchUsers = async (keyword) => {
  try {
    const response = await axios.get(`${API_URL}/search?keyword=${keyword}`, {
      headers: {
        Authorization: `Bearer ${getAuthToken()}`,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching users:", error);
    throw error;
  }
};