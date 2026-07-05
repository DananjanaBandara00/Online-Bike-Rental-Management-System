package service;

import dao.PaymentDAO;
import dao.FilePaymentDAO;
import model.Payment;
import model.Rental;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service class for payment-related operations.
 */
public class PaymentService {
    private PaymentDAO paymentDAO;
    private RentalService rentalService;
    
    /**
     * Constructor for PaymentService.
     */
    public PaymentService() {
        this.paymentDAO = new FilePaymentDAO();
        this.rentalService = new RentalService();
    }
    
    /**
     * Get all payments.
     * 
     * @return List of all payments
     */
    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }
    
    /**
     * Get a payment by its ID.
     * 
     * @param id The payment ID
     * @return The payment with the specified ID, or null if not found
     */
    public Payment getPaymentById(int id) {
        return paymentDAO.getPaymentById(id);
    }
    
    /**
     * Get all payments for a specific rental.
     * 
     * @param rentalId The rental ID
     * @return List of payments for the specified rental
     */
    public List<Payment> getPaymentsByRentalId(int rentalId) {
        return paymentDAO.getPaymentsByRentalId(rentalId);
    }
    
    /**
     * Get all payments for a specific user.
     * 
     * @param userId The user ID
     * @return List of payments for the specified user
     */
    public List<Payment> getPaymentsByUserId(int userId) {
        return paymentDAO.getPaymentsByUserId(userId);
    }
    
    /**
     * Process a payment for a rental.
     * 
     * @param rentalId The rental ID
     * @param userId The user ID
     * @param paymentMethod The payment method
     * @return The created payment, or null if the payment could not be processed
     */
    public Payment processPayment(int rentalId, int userId, String paymentMethod) {
        // Get the rental
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null || rental.isActive()) {
            return null;
        }
        
        // Check if payment already exists
        List<Payment> rentalPayments = paymentDAO.getPaymentsByRentalId(rentalId);
        if (!rentalPayments.isEmpty()) {
            return null; // Payment already exists
        }
        
        // Generate a transaction ID
        String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 6);
        
        // Create a new payment
        Payment payment = new Payment(
            0, // ID will be assigned by DAO
            rentalId,
            userId,
            rental.getTotalCost(),
            LocalDateTime.now(),
            paymentMethod,
            "Completed",
            transactionId
        );
        
        // Add the payment
        boolean added = paymentDAO.addPayment(payment);
        if (added) {
            // Get the payment with the assigned ID
            List<Payment> userPayments = paymentDAO.getPaymentsByUserId(userId);
            return userPayments.stream()
                    .filter(p -> p.getRentalId() == rentalId)
                    .findFirst()
                    .orElse(null);
        }
        
        return null;
    }
    
    /**
     * Refund a payment.
     * 
     * @param paymentId The payment ID
     * @return true if the payment was refunded successfully, false otherwise
     */
    public boolean refundPayment(int paymentId) {
        Payment payment = paymentDAO.getPaymentById(paymentId);
        if (payment == null || !"Completed".equals(payment.getStatus())) {
            return false;
        }
        
        // Update the payment status
        payment.setStatus("Refunded");
        
        // Update the payment
        return paymentDAO.updatePayment(payment);
    }
}
