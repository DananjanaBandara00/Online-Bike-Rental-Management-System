package dao;

import config.DatabaseConfig;
import model.Review;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File-based implementation of ReviewDAO.
 */
public class FileReviewDAO implements ReviewDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        
        // Ensure the file exists
        DatabaseConfig.ensureFileExists(DatabaseConfig.REVIEWS_FILE);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.REVIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    Review review = new Review(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]),
                        parts[5],
                        LocalDateTime.parse(parts[6], DATE_FORMATTER)
                    );
                    reviews.add(review);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return reviews;
    }

    @Override
    public Review getReviewById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.REVIEWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7 && Integer.parseInt(parts[0]) == id) {
                    return new Review(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]),
                        parts[5],
                        LocalDateTime.parse(parts[6], DATE_FORMATTER)
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Review> getReviewsByBikeId(int bikeId) {
        return getAllReviews().stream()
                .filter(review -> review.getBikeId() == bikeId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsByUserId(int userId) {
        return getAllReviews().stream()
                .filter(review -> review.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Review getReviewByRentalId(int rentalId) {
        return getAllReviews().stream()
                .filter(review -> review.getRentalId() == rentalId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addReview(Review review) {
        List<Review> reviews = getAllReviews();
        
        // Check if a review already exists for this rental
        boolean rentalReviewExists = reviews.stream()
                .anyMatch(r -> r.getRentalId() == review.getRentalId());
        
        if (rentalReviewExists) {
            return false; // Only one review per rental is allowed
        }
        
        // Generate a new ID (get the highest ID and add 1)
        int newId = 1; // Default if no reviews exist
        
        if (!reviews.isEmpty()) {
            newId = reviews.stream()
                    .mapToInt(Review::getId)
                    .max()
                    .orElse(0) + 1;
        }
        
        review.setId(newId);
        reviews.add(review);
        
        return saveAllReviews(reviews);
    }

    @Override
    public boolean updateReview(Review review) {
        List<Review> reviews = getAllReviews();
        boolean updated = false;
        
        // Replace the review with the updated one
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getId() == review.getId()) {
                reviews.set(i, review);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return saveAllReviews(reviews);
        }
        
        return false;
    }

    @Override
    public boolean deleteReview(int id) {
        List<Review> reviews = getAllReviews();
        boolean removed = reviews.removeIf(review -> review.getId() == id);
        
        if (removed) {
            return saveAllReviews(reviews);
        }
        
        return false;
    }
    
    /**
     * Save all reviews to the file.
     * 
     * @param reviews The list of reviews to save
     * @return true if the reviews were saved successfully, false otherwise
     */
    private boolean saveAllReviews(List<Review> reviews) {
        try {
            // Ensure directory exists
            DatabaseConfig.ensureFileExists(DatabaseConfig.REVIEWS_FILE);
            
            // Write all reviews to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseConfig.REVIEWS_FILE))) {
                for (Review review : reviews) {
                    writer.write(formatReviewForFile(review));
                    writer.newLine();
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Format a review for file storage.
     * 
     * @param review The review to format
     * @return The formatted string
     */
    private String formatReviewForFile(Review review) {
        return String.format("%d|%d|%d|%d|%d|%s|%s",
                review.getId(),
                review.getUserId(),
                review.getBikeId(),
                review.getRentalId(),
                review.getRating(),
                review.getComment(),
                review.getReviewDate().format(DATE_FORMATTER));
    }
}
