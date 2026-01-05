import OwnerSlotForm from "./OwnerSlotForm";
import "../styles/OwnerSlotCard.css";

function OwnerSlotCard({ slot, refreshSlots }) {
  return (
    <div className="owner-slot-card">
      <div className="owner-slot-card-header">
        <div>
          <h4 className="owner-slot-card-title">{slot.location}</h4>
          {slot.city && <span className="owner-slot-card-location">📍 {slot.city}</span>}
          {slot.address && <span className="owner-slot-card-location">📍 {slot.address}</span>}
        </div>
      </div>

      <div className="owner-slot-card-capacity">
        <div className="owner-slot-card-capacity-item">
          <span className="owner-slot-card-icon">🚗</span>
          <span>Car: {slot.carCapacity}</span>
        </div>
        <div className="owner-slot-card-capacity-item">
          <span className="owner-slot-card-icon">🏍️</span>
          <span>Bike: {slot.bikeCapacity}</span>
        </div>
        <div className="owner-slot-card-capacity-item">
          <span className="owner-slot-card-icon">🚚</span>
          <span>Truck: {slot.truckCapacity}</span>
        </div>
      </div>

      <OwnerSlotForm slot={slot} refreshSlots={refreshSlots} />
    </div>
  );
}

export default OwnerSlotCard;
