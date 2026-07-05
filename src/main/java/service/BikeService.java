package service;

import dao.BikeDAO;
import dao.FileBikeDAO;
import model.Bike;

import java.util.List;

/**
 * Service class for bike-related operations.
 */
public class BikeService {
    private BikeDAO bikeDAO;

    /**
     * Constructor for BikeService.
     */
    public BikeService() {
        this.bikeDAO = new FileBikeDAO();
    }

    /**
     * Get all bikes.
     *
     * @return List of all bikes
     */
    public List<Bike> getAllBikes() {
        return bikeDAO.getAllBikes();
    }

    /**
     * Get a bike by its ID.
     *
     * @param id The bike ID
     * @return The bike with the specified ID, or null if not found
     */
    public Bike getBikeById(int id) {
        return bikeDAO.getBikeById(id);
    }

    /**
     * Get all available bikes.
     *
     * @return List of all available bikes
     */
    public List<Bike> getAvailableBikes() {
        return bikeDAO.getAvailableBikes();
    }

    /**
     * Get bikes by type.
     *
     * @param type The bike type
     * @return List of bikes of the specified type
     */
    public List<Bike> getBikesByType(String type) {
        return bikeDAO.getBikesByType(type);
    }

    /**
     * Add a new bike.
     *
     * @param name The bike name
     * @param type The bike type
     * @param description The bike description
     * @param hourlyRate The hourly rental rate
     * @param dailyRate The daily rental rate
     * @param imageUrl The URL of the bike image
     * @return true if the bike was added successfully, false otherwise
     */
    public boolean addBike(String name, String type, String description,
                          double hourlyRate, double dailyRate, String imageUrl) {
        Bike bike = new Bike(0, name, type, description, hourlyRate, dailyRate, "Available", imageUrl);
        return bikeDAO.addBike(bike);
    }

    /**
     * Add a new bike directly from a Bike object.
     *
     * @param bike The bike to add
     * @return true if the bike was added successfully, false otherwise
     */
    public boolean addBike(Bike bike) {
        return bikeDAO.addBike(bike);
    }

    /**
     * Update an existing bike.
     *
     * @param bike The bike to update
     * @return true if the bike was updated successfully, false otherwise
     */
    public boolean updateBike(Bike bike) {
        return bikeDAO.updateBike(bike);
    }

    /**
     * Update a bike's status.
     *
     * @param bikeId The ID of the bike to update
     * @param status The new status
     * @return true if the bike was updated successfully, false otherwise
     */
    public boolean updateBikeStatus(int bikeId, String status) {
        Bike bike = getBikeById(bikeId);
        if (bike != null) {
            bike.setStatus(status);
            return bikeDAO.updateBike(bike);
        }
        return false;
    }

    /**
     * Delete a bike by its ID.
     *
     * @param bikeId The ID of the bike to delete
     * @return true if the bike was deleted successfully, false otherwise
     */
    public boolean deleteBike(int bikeId) {
        return bikeDAO.deleteBike(bikeId);
    }
}
