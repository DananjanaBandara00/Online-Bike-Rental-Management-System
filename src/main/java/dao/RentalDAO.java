package dao;

import model.Rental;
import java.util.List;

/**
 * Interface for Rental data access operations.
 */
public interface RentalDAO {
    /**
     * Get all rentals.
     * 
     * @return List of all rentals
     */
    List<Rental> getAllRentals();
    
    /**
     * Get a rental by its ID.
     * 
     * @param id The rental ID
     * @return The rental with the specified ID, or null if not found
     */
    Rental getRentalById(int id);
    
    /**
     * Get all rentals for a specific user.
     * 
     * @param userId The user ID
     * @return List of rentals for the specified user
     */
    List<Rental> getRentalsByUserId(int userId);
    
    /**
     * Get all rentals for a specific bike.
     * 
     * @param bikeId The bike ID
     * @return List of rentals for the specified bike
     */
    List<Rental> getRentalsByBikeId(int bikeId);
    
    /**
     * Get all active rentals.
     * 
     * @return List of all active rentals
     */
    List<Rental> getActiveRentals();
    
    /**
     * Add a new rental.
     * 
     * @param rental The rental to add
     * @return true if the rental was added successfully, false otherwise
     */
    boolean addRental(Rental rental);
    
    /**
     * Update an existing rental.
     * 
     * @param rental The rental to update
     * @return true if the rental was updated successfully, false otherwise
     */
    boolean updateRental(Rental rental);
    
    /**
     * Delete a rental by its ID.
     * 
     * @param id The ID of the rental to delete
     * @return true if the rental was deleted successfully, false otherwise
     */
    boolean deleteRental(int id);
}
