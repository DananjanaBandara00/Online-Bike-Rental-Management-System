package dao;

import model.Review;
import java.util.List;

/**
 * Interface for Review data access operations.
 */
public interface ReviewDAO {
    /**
     * Get all reviews.
     * 
     * @return List of all reviews
     */
    List<Review> getAllReviews();
    
    /**
     * Get a review by its ID.
     * 
     * @param id The review ID
     * @return The review with the specified ID, or null if not found
     */
    Review getReviewById(int id);
    
    /**
     * Get all reviews for a specific bike.
     * 
     * @param bikeId The bike ID
     * @return List of reviews for the specified bike
     */
    List<Review> getReviewsByBikeId(int bikeId);
    
    /**
     * Get all reviews by a specific user.
     * 
     * @param userId The user ID
     * @return List of reviews by the specified user
     */
    List<Review> getReviewsByUserId(int userId);
    
    /**
     * Get the review for a specific rental.
     * 
     * @param rentalId The rental ID
     * @return The review for the specified rental, or null if not found
     */
    Review getReviewByRentalId(int rentalId);
    
    /**
     * Add a new review.
     * 
     * @param review The review to add
     * @return true if the review was added successfully, false otherwise
     */
    boolean addReview(Review review);
    
    /**
     * Update an existing review.
     * 
     * @param review The review to update
     * @return true if the review was updated successfully, false otherwise
     */
    boolean updateReview(Review review);
    
    /**
     * Delete a review by its ID.
     * 
     * @param id The ID of the review to delete
     * @return true if the review was deleted successfully, false otherwise
     */
    boolean deleteReview(int id);
}
