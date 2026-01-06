const API_BASE = process.env.REACT_APP_API_URL || "http://localhost:8080";

export async function fetchSlotsForMap() {
  const res = await fetch(`${API_BASE}/api/slots/map`);
  return res.json();
}

export async function fetchSlotAvailability(slotId) {
  const res = await fetch(`${API_BASE}/api/slots/${slotId}/availability`);
  return res.json();
}

// Fetch all booking history for slots owned by the owner
export async function fetchOwnerSlotHistory(ownerId) {
  const res = await fetch(`${API_BASE}/api/owner/${ownerId}/slot-history`);
  if (!res.ok) throw new Error('Failed to fetch history');
  return res.json();
}
