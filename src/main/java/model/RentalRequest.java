package model;

import java.time.LocalDateTime;

/**
 * Represents a bike rental request in the queue.
 */
public class RentalRequest {
    private int id;
    private int userId;
    private int bikeId;
    private LocalDateTime requestTime;
    private String rentalType; // Hourly, Daily
    private String status; // Pending, Approved, Rejected
    
    /**
     * Constructor for RentalRequest.
     */
    public RentalRequest(int id, int userId, int bikeId, LocalDateTime requestTime, 
                        String rentalType, String status) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.requestTime = requestTime;
        this.rentalType = rentalType;
        this.status = status;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }
    
    public LocalDateTime getRequestTime() { return requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime; }
    
    public String getRentalType() { return rentalType; }
    public void setRentalType(String rentalType) { this.rentalType = rentalType; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    /**
     * Checks if the request is pending.
     * 
     * @return true if the request is pending, false otherwise
     */
    public boolean isPending() {
        return "Pending".equalsIgnoreCase(status);
    }
    
    /**
     * Checks if the request is approved.
     * 
     * @return true if the request is approved, false otherwise
     */
    public boolean isApproved() {
        return "Approved".equalsIgnoreCase(status);
    }
    
    /**
     * Checks if the request is rejected.
     * 
     * @return true if the request is rejected, false otherwise
     */
    public boolean isRejected() {
        return "Rejected".equalsIgnoreCase(status);
    }
}
