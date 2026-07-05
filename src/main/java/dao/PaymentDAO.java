package dao;

import model.Payment;
import java.util.List;

/**
 * Interface for Payment data access operations.
 */
public interface PaymentDAO {
    /**
     * Get all payments.
     * 
     * @return List of all payments
     */
    List<Payment> getAllPayments();
    
    /**
     * Get a payment by its ID.
     * 
     * @param id The payment ID
     * @return The payment with the specified ID, or null if not found
     */
    Payment getPaymentById(int id);
    
    /**
     * Get all payments for a specific rental.
     * 
     * @param rentalId The rental ID
     * @return List of payments for the specified rental
     */
    List<Payment> getPaymentsByRentalId(int rentalId);
    
    /**
     * Get all payments for a specific user.
     * 
     * @param userId The user ID
     * @return List of payments for the specified user
     */
    List<Payment> getPaymentsByUserId(int userId);
    
    /**
     * Add a new payment.
     * 
     * @param payment The payment to add
     * @return true if the payment was added successfully, false otherwise
     */
    boolean addPayment(Payment payment);
    
    /**
     * Update an existing payment.
     * 
     * @param payment The payment to update
     * @return true if the payment was updated successfully, false otherwise
     */
    boolean updatePayment(Payment payment);
    
    /**
     * Delete a payment by its ID.
     * 
     * @param id The ID of the payment to delete
     * @return true if the payment was deleted successfully, false otherwise
     */
    boolean deletePayment(int id);
}
