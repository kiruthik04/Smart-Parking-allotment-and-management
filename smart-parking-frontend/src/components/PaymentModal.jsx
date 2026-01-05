import React, { useState } from 'react';
import '../styles/PaymentModal.css';

const PaymentModal = ({ booking, onClose, onPaymentSuccess }) => {
    const [step, setStep] = useState('app-selection'); // app-selection, upi-id, request-sent, success
    const [selectedApp, setSelectedApp] = useState(null);
    const [upiId, setUpiId] = useState('');

    // Mock auto-complete UPI suggestions
    const upiSuggestions = [
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @okaxis`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @okhdfcbank`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @oksbi`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @okicici`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @ybl`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @axl`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @ibl`,
        `${booking.userName.toLowerCase().replace(/\s/g, '')} @paytm`
    ];

    const handleAppSelect = (app) => {
        setSelectedApp(app);
        if (app === 'gpay') {
            setUpiId(upiSuggestions[0]); // Auto-fill for demo
        }
        setStep('upi-id');
    };

    // This function handles the "Payment Approved" simulation
    // We wrap it in useCallback so it doesn't get re-created on every render
    const handleSimulateApproval = React.useCallback(() => {
        setStep('processing');

        // First timeout: Simulate "Verifying..." state
        setTimeout(() => {
            setStep('success');

            // Second timeout: Close modal after success message
            setTimeout(() => {
                onPaymentSuccess(); // Tell the parent component (BookingPage) to update UI
                onClose();          // Close this modal
            }, 2000);
        }, 1500);
    }, [onPaymentSuccess, onClose]);

    // Auto-advance simulation after request is sent
    // This mimics the "Polling" process where we check if payment is done
    React.useEffect(() => {
        if (step === 'request-sent') {
            // Wait 5 seconds to simulate user opening 'GPay' and approving
            const timer = setTimeout(() => {
                handleSimulateApproval();
            }, 5000);

            // Cleanup: If user closes modal early, cancel the timer
            return () => clearTimeout(timer);
        }
    }, [step, handleSimulateApproval]);

    const handleSendRequest = () => {
        setStep('request-sent');
        // Simulate waiting for external approval
    };

    if (step === 'success') {
        return (
            <div className="payment-modal-overlay">
                <div className="payment-modal-content success">
                    <div className="success-icon">✅</div>
                    <h2>Payment Successful!</h2>
                    <p>Amount paid: ₹{booking.totalPrice}</p>
                </div>
            </div>
        );
    }

    if (step === 'processing') {
        return (
            <div className="payment-modal-overlay">
                <div className="payment-modal-content processing">
                    <div className="spinner"></div>
                    <h2>Verifying Payment...</h2>
                    <p>Please wait while we confirm with the bank.</p>
                </div>
            </div>
        );
    }

    return (
        <div className="payment-modal-overlay">
            <div className="payment-modal-content">
                <button className="close-button" onClick={onClose}>×</button>

                {step === 'app-selection' && (
                    <>
                        <h2>Select Payment Method</h2>
                        <p className="amount-display">To Pay: ₹{booking.totalPrice}</p>
                        <div className="payment-apps-grid">
                            <button className="app-btn gpay" onClick={() => handleAppSelect('gpay')}>
                                <span role="img" aria-label="gpay">🇬</span> Google Pay
                            </button>
                            <button className="app-btn phonepe" onClick={() => handleAppSelect('phonepe')}>
                                <span role="img" aria-label="phonepe">🇵</span> PhonePe
                            </button>
                            <button className="app-btn paytm" onClick={() => handleAppSelect('paytm')}>
                                <span role="img" aria-label="paytm">💰</span> Paytm
                            </button>
                        </div>
                    </>
                )}

                {step === 'upi-id' && (
                    <>
                        <h2>Enter UPI ID</h2>
                        <p className="app-selected">Paying via {selectedApp === 'gpay' ? 'Google Pay' : selectedApp}</p>
                        <div className="input-group">
                            <label>Your UPI ID</label>
                            <input
                                type="text"
                                value={upiId}
                                onChange={(e) => setUpiId(e.target.value)}
                                list="upi-suggestions"
                            />
                            <datalist id="upi-suggestions">
                                {upiSuggestions.map(id => <option key={id} value={id} />)}
                            </datalist>
                        </div>
                        <button className="action-btn" onClick={handleSendRequest}>Send Payment Request</button>
                    </>
                )}

                {step === 'request-sent' && (
                    <>
                        <h2>Request Sent 📱</h2>
                        <p className="request-sent-text">
                            We've sent a payment request of <strong>₹{booking.totalPrice}</strong> to <strong>{upiId}</strong>.
                        </p>
                        <p className="request-sent-subtext">
                            Please open your {selectedApp === 'gpay' ? 'Google Pay' : selectedApp} app and approve the request.
                        </p>

                        <div className="simulation-controls">
                            <div className="payment-loading-status">
                                <div className="spinner-small"></div>
                                <p>Waiting for bank confirmation...</p>
                            </div>
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default PaymentModal;
