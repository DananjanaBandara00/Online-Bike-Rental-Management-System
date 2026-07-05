package dao;

import model.RentalRequest;
import java.util.List;

/**
 * Interface for RentalRequest data access operations.
 */
public interface RentalRequestDAO {
    /**
     * Get all rental requests.
     * 
     * @return List of all rental requests
     */
    List<RentalRequest> getAllRentalRequests();
    
    /**
     * Get a rental request by its ID.
     * 
     * @param id The rental request ID
     * @return The rental request with the specified ID, or null if not found
     */
    RentalRequest getRentalRequestById(int id);
    
    /**
     * Get all rental requests for a specific user.
     * 
     * @param userId The user ID
     * @return List of rental requests for the specified user
     */
    List<RentalRequest> getRentalRequestsByUserId(int userId);
    
    /**
     * Get all rental requests for a specific bike.
     * 
     * @param bikeId The bike ID
     * @return List of rental requests for the specified bike
     */
    List<RentalRequest> getRentalRequestsByBikeId(int bikeId);
    
    /**
     * Get all rental requests with a specific status.
     * 
     * @param status The status
     * @return List of rental requests with the specified status
     */
    List<RentalRequest> getRentalRequestsByStatus(String status);
    
    /**
     * Add a new rental request.
     * 
     * @param rentalRequest The rental request to add
     * @return true if the rental request was added successfully, false otherwise
     */
    boolean addRentalRequest(RentalRequest rentalRequest);
    
    /**
     * Update an existing rental request.
     * 
     * @param rentalRequest The rental request to update
     * @return true if the rental request was updated successfully, false otherwise
     */
    boolean updateRentalRequest(RentalRequest rentalRequest);
    
    /**
     * Delete a rental request by its ID.
     * 
     * @param id The ID of the rental request to delete
     * @return true if the rental request was deleted successfully, false otherwise
     */
    boolean deleteRentalRequest(int id);
}
