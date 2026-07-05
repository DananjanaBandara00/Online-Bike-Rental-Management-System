package model;

import java.time.LocalDateTime;

/**
 * Represents a review for a bike rental.
 */
public class Review {
    private int id;
    private int userId;
    private int bikeId;
    private int rentalId;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDateTime reviewDate;
    
    /**
     * Constructor for Review.
     */
    public Review(int id, int userId, int bikeId, int rentalId, int rating, String comment, LocalDateTime reviewDate) {
        this.id = id;
        this.userId = userId;
        this.bikeId = bikeId;
        this.rentalId = rentalId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getBikeId() { return bikeId; }
    public void setBikeId(int bikeId) { this.bikeId = bikeId; }
    
    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }
}
