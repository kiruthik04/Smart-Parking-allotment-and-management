import axios from "axios";

// -------------------------------
// Central Axios Instance
// -------------------------------
const API = axios.create({
  baseURL: "http://localhost:8080"
});

// -------------------------------
// Attach JWT Automatically
// -------------------------------
API.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// -------------------------------
// SLOT APIs
// -------------------------------
export const getSlots = () =>
  API.get("/api/slots");

export const getOwnerSlots = () =>
  API.get("/api/slots/owner");

export const getOwnerSummary = () =>
  API.get("/api/slots/owner/summary");

export const getSlotsForMap = () =>
  API.get("/api/slots/map");

export const getSlotAvailability = (slotId) =>
  API.get(`/api/slots/${slotId}/availability`);

// -------------------------------
// BOOKING APIs
// -------------------------------
export const bookSlot = (data) =>
  API.post("/api/bookings", data);

export const getMyBookings = () =>
  API.get("/api/bookings/me");

export const getOwnerBookings = () =>
  API.get("/api/bookings/owner");

export const endBooking = (bookingId) =>
  API.post(`/api/bookings/${bookingId}/end`);

export const payBooking = (bookingId) =>
  API.post(`/api/bookings/${bookingId}/pay`);

// -------------------------------
// AUTH APIs (OPTIONAL FRONTEND USE)
// -------------------------------
export const signup = (data) =>
  API.post("/api/users/signup", data);

export const login = (data) =>
  API.post("/api/users/login", data);

export const updateSlotCapacity = (slotId, car, bike, truck) =>
  API.put(`/api/slots/${slotId}/capacity`, null, {
    params: { car, bike, truck }
  });

export const enableSlot = (slotId) =>
  API.put(`/api/slots/${slotId}/enable`, {});

export const disableSlot = (slotId) =>
  API.put(`/api/slots/${slotId}/disable`, {});

export const createSlot = (data) =>
  API.post(`/api/slots`, data);

export const updateSlotPrice = (slotId, carPrice, bikePrice, truckPrice) =>
  API.put(`/api/slots/${slotId}/price`, null, {
    params: { carPrice, bikePrice, truckPrice }
  });

export const updateSlotUpiId = (slotId, upiId) =>
  API.put(`/api/slots/${slotId}/upi`, null, {
    params: { upiId }
  });

export const deleteSlot = (slotId) =>
  API.delete(`/api/slots/${slotId}`);

// -------------------------------
// USER PROFILE APIs
// -------------------------------
export const getMyProfile = () =>
  API.get("/api/users/me");

export const updateMyProfile = (data) =>
  API.put("/api/users/me", data);

// -------------------------------
// VEHICLE APIs
// -------------------------------
export const getMyVehicles = () =>
  API.get("/api/vehicles/me");

export const deleteVehicle = (vehicleId) =>
  API.delete(`/api/vehicles/${vehicleId}`);

export const createVehicle = (data) =>
  API.post("/api/vehicles", data);


export default API;
