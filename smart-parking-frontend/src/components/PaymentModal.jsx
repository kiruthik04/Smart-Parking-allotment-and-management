import React, { useState } from 'react';
import '../styles/PaymentModal.css';

const PaymentModal = ({ booking, onClose, onPaymentSuccess }) => {
    const [utr, setUtr] = useState('');
    const [error, setError] = useState('');

    const handleSubmitUtr = () => {
        if (!utr || utr.length < 4) {
            setError("Please enter a valid UTR / Transaction No.");
            return;
        }
        // Calls parent handler with the manual UTR
        onPaymentSuccess(utr);
        onClose();
    };

    const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=upi://pay?pa=${booking.upiId}&pn=${booking.userName}&am=${booking.totalPrice}&cu=INR`;

    return (
        <div className="payment-modal-overlay">
            <div className="payment-modal-content">
                <button className="close-button" onClick={onClose}>×</button>

                <h2>Scan & Pay</h2>
                <p className="amount-display">Amount: ₹{booking.totalPrice}</p>
                <div style={{ textAlign: 'center', margin: '20px 0' }}>
                    <div style={{ background: 'white', padding: '10px', display: 'inline-block', borderRadius: '8px', border: '1px solid #ddd' }}>
                        <img src={qrUrl} alt="Payment QR" style={{ width: '200px', height: '200px' }} />
                    </div>
                </div>

                <p style={{ textAlign: 'center', fontSize: '0.9em', color: '#555' }}>
                    1. Scan with any UPI App (GPay, PhonePe, Paytm).<br />
                    2. Complete the payment of <b>₹{booking.totalPrice}</b>.<br />
                    3. Enter the <b>Transaction ID / UTR</b> below to confirm.
                </p>

                <div className="input-group" style={{ marginTop: '20px' }}>
                    <label>Enter UTR / Reference No:</label>
                    <input
                        type="text"
                        value={utr}
                        onChange={(e) => {
                            setUtr(e.target.value);
                            setError('');
                        }}
                        placeholder="e.g. 3289XXXXXXX"
                        className="utr-input"
                        style={{ width: '100%', padding: '10px', marginTop: '5px', borderRadius: '4px', border: '1px solid #ccc' }}
                    />
                    {error && <p style={{ color: 'red', fontSize: '0.85em', marginTop: '5px' }}>{error}</p>}
                </div>

                <div style={{ display: 'flex', gap: '10px', marginTop: '20px' }}>
                    <button className="action-btn" onClick={handleSubmitUtr} style={{ flex: 1 }}>
                        ✅ Confirm Payment
                    </button>
                    <button className="action-btn" onClick={onClose} style={{ flex: 1, background: '#64748b' }}>
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
};

export default PaymentModal;
