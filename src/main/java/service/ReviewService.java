package service;

import dao.ReviewDAO;
import dao.FileReviewDAO;
import model.Review;
import model.Rental;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for review-related operations.
 */
public class ReviewService {
    private ReviewDAO reviewDAO;
    private RentalService rentalService;
    
    /**
     * Constructor for ReviewService.
     */
    public ReviewService() {
        this.reviewDAO = new FileReviewDAO();
        this.rentalService = new RentalService();
    }
    
    /**
     * Get all reviews.
     * 
     * @return List of all reviews
     */
    public List<Review> getAllReviews() {
        return reviewDAO.getAllReviews();
    }
    
    /**
     * Get a review by its ID.
     * 
     * @param id The review ID
     * @return The review with the specified ID, or null if not found
     */
    public Review getReviewById(int id) {
        return reviewDAO.getReviewById(id);
    }
    
    /**
     * Get all reviews for a specific bike.
     * 
     * @param bikeId The bike ID
     * @return List of reviews for the specified bike
     */
    public List<Review> getReviewsByBikeId(int bikeId) {
        return reviewDAO.getReviewsByBikeId(bikeId);
    }
    
    /**
     * Get all reviews by a specific user.
     * 
     * @param userId The user ID
     * @return List of reviews by the specified user
     */
    public List<Review> getReviewsByUserId(int userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }
    
    /**
     * Get the review for a specific rental.
     * 
     * @param rentalId The rental ID
     * @return The review for the specified rental, or null if not found
     */
    public Review getReviewByRentalId(int rentalId) {
        return reviewDAO.getReviewByRentalId(rentalId);
    }
    
    /**
     * Add a review for a rental.
     * 
     * @param userId The user ID
     * @param rentalId The rental ID
     * @param rating The rating (1-5)
     * @param comment The review comment
     * @return The created review, or null if the review could not be created
     */
    public Review addReview(int userId, int rentalId, int rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            return null;
        }
        
        // Check if the rental exists and is completed
        Rental rental = rentalService.getRentalById(rentalId);
        if (rental == null || rental.isActive() || rental.getUserId() != userId) {
            return null;
        }
        
        // Check if a review already exists for this rental
        Review existingReview = reviewDAO.getReviewByRentalId(rentalId);
        if (existingReview != null) {
            return null; // Review already exists
        }
        
        // Create a new review
        Review review = new Review(
            0, // ID will be assigned by DAO
            userId,
            rental.getBikeId(),
            rentalId,
            rating,
            comment,
            LocalDateTime.now()
        );
        
        // Add the review
        boolean added = reviewDAO.addReview(review);
        if (added) {
            return reviewDAO.getReviewByRentalId(rentalId);
        }
        
        return null;
    }
    
    /**
     * Update a review.
     * 
     * @param reviewId The review ID
     * @param rating The new rating
     * @param comment The new comment
     * @return The updated review, or null if the review could not be updated
     */
    public Review updateReview(int reviewId, int rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            return null;
        }
        
        // Get the existing review
        Review review = reviewDAO.getReviewById(reviewId);
        if (review == null) {
            return null;
        }
        
        // Update the review
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(LocalDateTime.now());
        
        // Save the updated review
        boolean updated = reviewDAO.updateReview(review);
        if (updated) {
            return reviewDAO.getReviewById(reviewId);
        }
        
        return null;
    }
    
    /**
     * Delete a review.
     * 
     * @param reviewId The review ID
     * @return true if the review was deleted successfully, false otherwise
     */
    public boolean deleteReview(int reviewId) {
        return reviewDAO.deleteReview(reviewId);
    }
    
    /**
     * Calculate the average rating for a bike.
     * 
     * @param bikeId The bike ID
     * @return The average rating, or 0 if no reviews exist
     */
    public double getAverageRatingForBike(int bikeId) {
        List<Review> reviews = reviewDAO.getReviewsByBikeId(bikeId);
        if (reviews.isEmpty()) {
            return 0;
        }
        
        double sum = reviews.stream()
                .mapToInt(Review::getRating)
                .sum();
        
        return sum / reviews.size();
    }
}
