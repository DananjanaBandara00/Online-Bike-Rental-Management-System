package model;

/**
 * Represents a bike in the rental system.
 */
public class Bike {
    private int id;
    private String name;
    private String type;
    private String description;
    private double hourlyRate;
    private double dailyRate;
    private String status; // Available, Rented, Maintenance
    private String imageUrl;
    
    /**
     * Constructor for Bike.
     */
    public Bike(int id, String name, String type, String description, 
                double hourlyRate, double dailyRate, String status, String imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.hourlyRate = hourlyRate;
        this.dailyRate = dailyRate;
        this.status = status;
        this.imageUrl = imageUrl;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    
    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    /**
     * Checks if the bike is available for rental.
     * 
     * @return true if the bike is available, false otherwise
     */
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(status);
    }
}
