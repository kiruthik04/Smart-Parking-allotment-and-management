import { useState } from "react";
import { updateSlotCapacity, enableSlot, disableSlot, updateSlotPrice, deleteSlot, updateSlotUpiId } from "../services/api";
import "../styles/OwnerSlotForm.css";

function OwnerSlotForm({ slot, refreshSlots }) {
  const [car, setCar] = useState(slot.carCapacity);
  const [bike, setBike] = useState(slot.bikeCapacity);
  const [truck, setTruck] = useState(slot.truckCapacity);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  const updateCapacity = async () => {
    try {
      await updateSlotCapacity(slot.id, car, bike, truck);
      setMessage("Capacity updated successfully!");
      setMessageType("success");
      setTimeout(() => setMessage(""), 3000);
      if (refreshSlots) refreshSlots();
    } catch {
      setMessage("Failed to update capacity");
      setMessageType("error");
      setTimeout(() => setMessage(""), 3000);
    }
  };

  const [carPrice, setCarPrice] = useState(slot.carPricePerHour || 0);
  const [bikePrice, setBikePrice] = useState(slot.bikePricePerHour || 0);
  const [truckPrice, setTruckPrice] = useState(slot.truckPricePerHour || 0);

  const updatePrice = async () => {
    try {
      await updateSlotPrice(slot.id, carPrice, bikePrice, truckPrice);
      setMessage("Prices updated successfully!");
      setMessageType("success");
      setTimeout(() => setMessage(""), 3000);
      if (refreshSlots) refreshSlots();
    } catch {
      setMessage("Failed to update prices");
      setMessageType("error");
      setTimeout(() => setMessage(""), 3000);
    }
  };

  const [upiId, setUpiId] = useState(slot.upiId || "");

  const updateUpi = async () => {
    try {
      await updateSlotUpiId(slot.id, upiId);
      setMessage("UPI ID updated successfully!");
      setMessageType("success");
      setTimeout(() => setMessage(""), 3000);
      if (refreshSlots) refreshSlots();
    } catch {
      setMessage("Failed to update UPI ID");
      setMessageType("error");
      setTimeout(() => setMessage(""), 3000);
    }
  };

  const handleEnable = async () => {
    try {
      await enableSlot(slot.id);
      setMessage("Slot enabled successfully!");
      setMessageType("success");
      setTimeout(() => setMessage(""), 3000);
      if (refreshSlots) refreshSlots();
    } catch {
      setMessage("Failed to enable slot");
      setMessageType("error");
      setTimeout(() => setMessage(""), 3000);
    }
  };

  const handleDisable = async () => {
    try {
      await disableSlot(slot.id);
      setMessage("Slot disabled successfully!");
      setMessageType("success");
      setTimeout(() => setMessage(""), 3000);
      if (refreshSlots) refreshSlots();
    } catch {
      setMessage("Failed to disable slot");
      setMessageType("error");
      setTimeout(() => setMessage(""), 3000);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm("Are you sure you want to delete this parking slot? This action cannot be undone.")) {
      return;
    }
    try {
      await deleteSlot(slot.id);
      setMessage("Slot deleted successfully!");
      setMessageType("success");
      if (refreshSlots) refreshSlots();
    } catch (err) {
      setMessage(err.response?.data?.message || "Failed to delete slot. Make sure there are no active bookings.");
      setMessageType("error");
      setTimeout(() => setMessage(""), 5000);
    }
  };

  return (
    <div className="owner-slot-form-container">
      <div className="owner-slot-form-section">
        <h5 className="owner-slot-form-section-title">⚙️ Capacity Management</h5>
        <div className="owner-slot-form-row">
          <label className="owner-slot-form-label">
            Car Capacity
            <input
              type="number"
              value={car}
              onChange={e => setCar(Number(e.target.value))}
              className="owner-slot-form-input"
            />
          </label>
          <label className="owner-slot-form-label">
            Bike Capacity
            <input
              type="number"
              value={bike}
              onChange={e => setBike(Number(e.target.value))}
              className="owner-slot-form-input"
            />
          </label>
          <label className="owner-slot-form-label">
            Truck Capacity
            <input
              type="number"
              value={truck}
              onChange={e => setTruck(Number(e.target.value))}
              className="owner-slot-form-input"
            />
          </label>
        </div>
        <button
          onClick={updateCapacity}
          className="owner-slot-form-button owner-slot-form-button-primary"
        >
          Update Capacity
        </button>
      </div>

      <div className="owner-slot-form-section">
        <h5 className="owner-slot-form-section-title">💰 Hourly Pricing</h5>
        <div className="owner-slot-form-row">
          <label className="owner-slot-form-label">
            Car/hr (₹)
            <input
              type="number"
              value={carPrice}
              onChange={e => setCarPrice(Number(e.target.value))}
              className="owner-slot-form-input"
            />
          </label>
          <label className="owner-slot-form-label">
            Bike/hr (₹)
            <input
              type="number"
              value={bikePrice}
              onChange={e => setBikePrice(Number(e.target.value))}
              className="owner-slot-form-input"
            />
          </label>
          <label className="owner-slot-form-label">
            Truck/hr (₹)
            <input
              type="number"
              value={truckPrice}
              onChange={e => setTruckPrice(Number(e.target.value))}
              className="owner-slot-form-input"
            />
          </label>
        </div>
        <button
          onClick={updatePrice}
          className="owner-slot-form-button owner-slot-form-button-primary"
        >
          Update Prices
        </button>
      </div>

      <div className="owner-slot-form-section">
        <h5 className="owner-slot-form-section-title">💳 UPI Details</h5>
        <div className="owner-slot-form-row">
          <label className="owner-slot-form-label" style={{ flex: 1 }}>
            UPI ID
            <input
              type="text"
              value={upiId}
              placeholder="e.g. user@okhdfcbank"
              onChange={e => setUpiId(e.target.value)}
              className="owner-slot-form-input"
              style={{ width: '100%' }}
            />
          </label>
        </div>
        <button
          onClick={updateUpi}
          className="owner-slot-form-button owner-slot-form-button-primary"
        >
          Update UPI ID
        </button>
      </div>

      <div className="owner-slot-form-section">
        <h5 className="owner-slot-form-section-title" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          🔧 Slot Controls
          <span style={{
            fontSize: '12px',
            padding: '4px 8px',
            borderRadius: '12px',
            background: slot.enabled === false ? '#fee2e2' : '#dcfce7',
            color: slot.enabled === false ? '#991b1b' : '#166534',
            border: `1px solid ${slot.enabled === false ? '#fecaca' : '#bbf7d0'}`
          }}>
            {slot.enabled === false ? '⛔ Closed' : '✅ Active'}
          </span>
        </h5>
        <div className="owner-slot-form-button-group">
          <button
            onClick={handleEnable}
            disabled={slot.enabled !== false}
            className={`owner-slot-form-button owner-slot-form-button-secondary ${slot.enabled !== false ? 'disabled-button' : ''}`}
            style={{ opacity: slot.enabled !== false ? 0.6 : 1, cursor: slot.enabled !== false ? 'not-allowed' : 'pointer' }}
          >
            ✅ Enable
          </button>
          <button
            onClick={handleDisable}
            disabled={slot.enabled === false}
            className={`owner-slot-form-button owner-slot-form-button-warning ${slot.enabled === false ? 'disabled-button' : ''}`}
            style={{ opacity: slot.enabled === false ? 0.6 : 1, cursor: slot.enabled === false ? 'not-allowed' : 'pointer' }}
          >
            ⏸️ Disable
          </button>
          <button
            onClick={handleDelete}
            className="owner-slot-form-button owner-slot-form-button-danger"
          >
            🗑️ Delete Slot
          </button>
        </div>
      </div>

      {message && (
        <div className={`owner-slot-form-message ${messageType === 'success' ? 'owner-slot-form-message-success' : 'owner-slot-form-message-error'}`}>
          {message}
        </div>
      )}
    </div>
  );
}

export default OwnerSlotForm;
