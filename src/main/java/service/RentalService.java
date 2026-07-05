package service;

import dao.RentalDAO;
import dao.FileRentalDAO;
import model.Bike;
import model.Rental;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for rental-related operations.
 */
public class RentalService {
    private RentalDAO rentalDAO;
    private BikeService bikeService;

    /**
     * Constructor for RentalService.
     */
    public RentalService() {
        this.rentalDAO = new FileRentalDAO();
        this.bikeService = new BikeService();
    }

    /**
     * Get all rentals.
     *
     * @return List of all rentals
     */
    public List<Rental> getAllRentals() {
        return rentalDAO.getAllRentals();
    }

    /**
     * Get a rental by its ID.
     *
     * @param id The rental ID
     * @return The rental with the specified ID, or null if not found
     */
    public Rental getRentalById(int id) {
        return rentalDAO.getRentalById(id);
    }

    /**
     * Get all rentals for a specific user.
     *
     * @param userId The user ID
     * @return List of rentals for the specified user
     */
    public List<Rental> getRentalsByUserId(int userId) {
        return rentalDAO.getRentalsByUserId(userId);
    }

    /**
     * Get all active rentals for a specific user.
     *
     * @param userId The user ID
     * @return List of active rentals for the specified user
     */
    public List<Rental> getActiveRentalsByUserId(int userId) {
        return rentalDAO.getRentalsByUserId(userId).stream()
                .filter(Rental::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Start a new rental.
     *
     * @param userId The user ID
     * @param bikeId The bike ID
     * @param rentalType The rental type (Hourly, Daily)
     * @return The created rental, or null if the rental could not be created
     */
    public Rental startRental(int userId, int bikeId, String rentalType) {
        // Check if the bike is available
        Bike bike = bikeService.getBikeById(bikeId);
        if (bike == null || !bike.isAvailable()) {
            return null;
        }

        // Create a new rental
        Rental rental = new Rental(
            0, // ID will be assigned by DAO
            userId,
            bikeId,
            LocalDateTime.now(),
            null, // End time will be set when the rental is completed
            rentalType,
            0.0, // Total cost will be calculated when the rental is completed
            "Active"
        );

        // Add the rental
        boolean added = rentalDAO.addRental(rental);
        if (added) {
            // Update the bike status
            bikeService.updateBikeStatus(bikeId, "Rented");

            // Get the rental with the assigned ID
            List<Rental> userRentals = rentalDAO.getRentalsByUserId(userId);
            return userRentals.stream()
                    .filter(r -> r.getBikeId() == bikeId && r.isActive())
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    /**
     * End a rental.
     *
     * @param rentalId The rental ID
     * @return The updated rental with calculated cost, or null if the rental could not be updated
     */
    public Rental endRental(int rentalId) {
        Rental rental = rentalDAO.getRentalById(rentalId);
        if (rental == null || !rental.isActive()) {
            return null;
        }

        // Set the end time
        LocalDateTime endTime = LocalDateTime.now();
        rental.setEndTime(endTime);

        // Calculate the total cost
        Bike bike = bikeService.getBikeById(rental.getBikeId());
        if (bike == null) {
            return null;
        }

        double totalCost = calculateRentalCost(rental, bike);
        rental.setTotalCost(totalCost);

        // Update the rental status
        rental.setStatus("Completed");

        // Update the rental
        boolean updated = rentalDAO.updateRental(rental);
        if (updated) {
            // Update the bike status
            bikeService.updateBikeStatus(rental.getBikeId(), "Available");
            return rental;
        }

        return null;
    }

    /**
     * Calculate the total cost of a rental.
     *
     * @param rental The rental
     * @param bike The rented bike
     * @return The total cost
     */
    private double calculateRentalCost(Rental rental, Bike bike) {
        LocalDateTime startTime = rental.getStartTime();
        LocalDateTime endTime = rental.getEndTime();

        if ("Hourly".equals(rental.getRentalType())) {
            // Calculate hours (rounded up)
            long hours = startTime.until(endTime, ChronoUnit.HOURS);
            if (startTime.until(endTime, ChronoUnit.MINUTES) % 60 > 0) {
                hours++;
            }
            return hours * bike.getHourlyRate();
        } else if ("Daily".equals(rental.getRentalType())) {
            // Calculate days (rounded up)
            long days = startTime.until(endTime, ChronoUnit.DAYS);
            if (startTime.until(endTime, ChronoUnit.HOURS) % 24 > 0) {
                days++;
            }
            return days * bike.getDailyRate();
        }

        return 0.0;
    }

    /**
     * Cancel a rental.
     *
     * @param rentalId The rental ID
     * @return true if the rental was cancelled successfully, false otherwise
     */
    public boolean cancelRental(int rentalId) {
        Rental rental = rentalDAO.getRentalById(rentalId);
        if (rental == null || !rental.isActive()) {
            return false;
        }

        // Update the rental status
        rental.setStatus("Cancelled");

        // Update the rental
        boolean updated = rentalDAO.updateRental(rental);
        if (updated) {
            // Update the bike status
            bikeService.updateBikeStatus(rental.getBikeId(), "Available");
            return true;
        }

        return false;
    }

    /**
     * Delete a rental record from history.
     * This should only be used for completed or cancelled rentals.
     *
     * @param rentalId The rental ID
     * @return true if the rental was deleted successfully, false otherwise
     */
    public boolean deleteRental(int rentalId) {
        Rental rental = rentalDAO.getRentalById(rentalId);
        if (rental == null || rental.isActive()) {
            return false;
        }

        // Only allow deletion of completed or cancelled rentals
        if (!"Completed".equals(rental.getStatus()) && !"Cancelled".equals(rental.getStatus())) {
            return false;
        }

        // Delete the rental
        return rentalDAO.deleteRental(rentalId);
    }
}
