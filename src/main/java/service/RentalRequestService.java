package service;

import dao.RentalRequestDAO;
import dao.FileRentalRequestDAO;
import model.RentalRequest;
import model.Bike;
import model.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for rental request operations.
 */
public class RentalRequestService {
    private RentalRequestDAO rentalRequestDAO;
    private BikeService bikeService;
    private RentalService rentalService;
    
    /**
     * Constructor for RentalRequestService.
     */
    public RentalRequestService() {
        this.rentalRequestDAO = new FileRentalRequestDAO();
        this.bikeService = new BikeService();
        this.rentalService = new RentalService();
    }
    
    /**
     * Get all rental requests.
     * 
     * @return List of all rental requests
     */
    public List<RentalRequest> getAllRentalRequests() {
        return rentalRequestDAO.getAllRentalRequests();
    }
    
    /**
     * Get a rental request by its ID.
     * 
     * @param id The rental request ID
     * @return The rental request with the specified ID, or null if not found
     */
    public RentalRequest getRentalRequestById(int id) {
        return rentalRequestDAO.getRentalRequestById(id);
    }
    
    /**
     * Get all rental requests for a specific user.
     * 
     * @param userId The user ID
     * @return List of rental requests for the specified user
     */
    public List<RentalRequest> getRentalRequestsByUserId(int userId) {
        return rentalRequestDAO.getRentalRequestsByUserId(userId);
    }
    
    /**
     * Get all rental requests for a specific bike.
     * 
     * @param bikeId The bike ID
     * @return List of rental requests for the specified bike
     */
    public List<RentalRequest> getRentalRequestsByBikeId(int bikeId) {
        return rentalRequestDAO.getRentalRequestsByBikeId(bikeId);
    }
    
    /**
     * Get all pending rental requests.
     * queue implementation
     * @return List of pending rental requests
     */
    public List<RentalRequest> getPendingRentalRequests() {
        return rentalRequestDAO.getRentalRequestsByStatus("Pending");
    }
    
    /**
     * Create a new rental request.
     * 
     * @param userId The user ID
     * @param bikeId The bike ID
     * @param rentalType The rental type (Hourly, Daily)
     * @return The created rental request, or null if the request could not be created
     */
    public RentalRequest createRentalRequest(int userId, int bikeId, String rentalType) {
        // Check if the bike exists
        Bike bike = bikeService.getBikeById(bikeId);
        if (bike == null) {
            return null;
        }
        
        // Create a new rental request
        RentalRequest rentalRequest = new RentalRequest(
            0, // ID will be assigned by DAO
            userId,
            bikeId,
            LocalDateTime.now(),
            rentalType,
            "Pending"
        );
        
        // Add the rental request
        boolean added = rentalRequestDAO.addRentalRequest(rentalRequest);
        if (added) {
            // Get the rental request with the assigned ID
            List<RentalRequest> userRequests = rentalRequestDAO.getRentalRequestsByUserId(userId);
            return userRequests.stream()
                    .filter(r -> r.getBikeId() == bikeId && r.isPending())
                    .findFirst()
                    .orElse(null);
        }
        
        return null;
    }
    
    /**
     * Approve a rental request.
     * 
     * @param requestId The rental request ID
     * @return true if the request was approved successfully, false otherwise
     */
    public boolean approveRentalRequest(int requestId) {
        RentalRequest request = rentalRequestDAO.getRentalRequestById(requestId);
        if (request == null || !request.isPending()) {
            return false;
        }
        
        // Check if the bike is available
        Bike bike = bikeService.getBikeById(request.getBikeId());
        if (bike == null || !bike.isAvailable()) {
            return false;
        }
        
        // Start the rental
        if (rentalService.startRental(request.getUserId(), request.getBikeId(), request.getRentalType()) != null) {
            // Update the request status
            request.setStatus("Approved");
            return rentalRequestDAO.updateRentalRequest(request);
        }
        
        return false;
    }
    
    /**
     * Reject a rental request.
     * 
     * @param requestId The rental request ID
     * @return true if the request was rejected successfully, false otherwise
     */
    public boolean rejectRentalRequest(int requestId) {
        RentalRequest request = rentalRequestDAO.getRentalRequestById(requestId);
        if (request == null || !request.isPending()) {
            return false;
        }
        
        // Update the request status
        request.setStatus("Rejected");
        return rentalRequestDAO.updateRentalRequest(request);
    }
    
    /**
     * Cancel a rental request.
     * 
     * @param requestId The rental request ID
     * @return true if the request was cancelled successfully, false otherwise
     */
    public boolean cancelRentalRequest(int requestId) {
        RentalRequest request = rentalRequestDAO.getRentalRequestById(requestId);
        if (request == null || !request.isPending()) {
            return false;
        }
        
        // Delete the request
        return rentalRequestDAO.deleteRentalRequest(requestId);
    }
    
    /**
     * Sort bikes by availability using QuickSort algorithm.
     * Available bikes will be listed first, followed by rented and maintenance bikes.
     * 
     * @param bikes The list of bikes to sort
     * @return The sorted list of bikes
     */
    public List<Bike> sortBikesByAvailability(List<Bike> bikes) {
        if (bikes == null || bikes.size() <= 1) {
            return bikes;
        }
        
        quickSortByAvailability(bikes, 0, bikes.size() - 1);
        return bikes;
    }
    
    /**
     * QuickSort implementation for sorting bikes by availability.
     * 
     * @param bikes The list of bikes to sort
     * @param low The low index
     * @param high The high index
     */
    private void quickSortByAvailability(List<Bike> bikes, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(bikes, low, high);
            quickSortByAvailability(bikes, low, pivotIndex - 1);
            quickSortByAvailability(bikes, pivotIndex + 1, high);
        }
    }
    
    /**
     * Partition function for QuickSort.
     * 
     * @param bikes The list of bikes to partition
     * @param low The low index
     * @param high The high index
     * @return The pivot index
     */
    private int partition(List<Bike> bikes, int low, int high) {
        Bike pivot = bikes.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            // Compare availability: available bikes come first
            if (compareAvailability(bikes.get(j), pivot) <= 0) {
                i++;
                swap(bikes, i, j);
            }
        }
        
        swap(bikes, i + 1, high);
        return i + 1;
    }
    
    /**
     * Compare bikes by availability.
     * Available bikes come first, followed by rented and maintenance bikes.
     * 
     * @param bike1 The first bike
     * @param bike2 The second bike
     * @return -1 if bike1 should come before bike2, 1 if bike1 should come after bike2, 0 if they are equal
     */
    private int compareAvailability(Bike bike1, Bike bike2) {
        if (bike1.isAvailable() && !bike2.isAvailable()) {
            return -1;
        } else if (!bike1.isAvailable() && bike2.isAvailable()) {
            return 1;
        } else {
            return 0;
        }
    }
    
    /**
     * Swap two elements in a list.
     * 
     * @param list The list
     * @param i The first index
     * @param j The second index
     */
    private <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
