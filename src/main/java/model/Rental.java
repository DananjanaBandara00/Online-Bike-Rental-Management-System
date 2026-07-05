package model;

import java.time.LocalDateTime;

/**
 * Represents a bike rental transaction.
 */
public class Rental {
    private int id;
    private int userId;
    private int bikeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String rentalType; // Hourly, Daily
    private double totalCost;
    private String status; // Active, Completed, Cancelled
    
    /**
     * Constructor for Rental.
     */
    public Rental(int id, int userId, int bikeId, LocalDateTime startTime, LocalDateTime endTime, 
                 String rentalType, double totalCost, String status) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rentalType = rentalType;
        this.totalCost = totalCost;
        this.status = status;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public String getRentalType() { return rentalType; }
    public void setRentalType(String rentalType) { this.rentalType = rentalType; }
    
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    /**
     * Checks if the rental is active.
     * 
     * @return true if the rental is active, false otherwise
     */
    public boolean isActive() {
        return "Active".equalsIgnoreCase(status);
    }
}
