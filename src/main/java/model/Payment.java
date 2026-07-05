package model;

import java.time.LocalDateTime;

/**
 * Represents a payment for a rental.
 */
public class Payment {
    private int id;
    private int rentalId;
    private int userId;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod; // Credit Card, PayPal, Cash
    private String status; // Completed, Pending, Failed
    private String transactionId;
    
    /**
     * Constructor for Payment.
     */
    public Payment(int id, int rentalId, int userId, double amount, LocalDateTime paymentDate, 
                  String paymentMethod, String status, String transactionId) {
        this.id = id;
        this.rentalId = rentalId;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.transactionId = transactionId;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    /**
     * Checks if the payment is completed.
     * 
     * @return true if the payment is completed, false otherwise
     */
    public boolean isCompleted() {
        return "Completed".equalsIgnoreCase(status);
    }
}
