package dao;

import config.DatabaseConfig;
import model.RentalRequest;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File-based implementation of RentalRequestDAO.
 */
public class FileRentalRequestDAO implements RentalRequestDAO {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public List<RentalRequest> getAllRentalRequests() {
        List<RentalRequest> rentalRequests = new ArrayList<>();
        
        // Ensure the file exists
        DatabaseConfig.ensureFileExists(DatabaseConfig.RENTAL_REQUESTS_FILE);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.RENTAL_REQUESTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    RentalRequest rentalRequest = new RentalRequest(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER),
                        parts[4],
                        parts[5]
                    );
                    rentalRequests.add(rentalRequest);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return rentalRequests;
    }

    @Override
    public RentalRequest getRentalRequestById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.RENTAL_REQUESTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6 && Integer.parseInt(parts[0]) == id) {
                    return new RentalRequest(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        LocalDateTime.parse(parts[3], DATE_TIME_FORMATTER),
                        parts[4],
                        parts[5]
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<RentalRequest> getRentalRequestsByUserId(int userId) {
        return getAllRentalRequests().stream()
                .filter(request -> request.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalRequest> getRentalRequestsByBikeId(int bikeId) {
        return getAllRentalRequests().stream()
                .filter(request -> request.getBikeId() == bikeId)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentalRequest> getRentalRequestsByStatus(String status) {
        return getAllRentalRequests().stream()
                .filter(request -> request.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addRentalRequest(RentalRequest rentalRequest) {
        List<RentalRequest> rentalRequests = getAllRentalRequests();
        
        // Generate a new ID (get the highest ID and add 1)
        int newId = 1; // Default if no rental requests exist
        
        if (!rentalRequests.isEmpty()) {
            newId = rentalRequests.stream()
                    .mapToInt(RentalRequest::getId)
                    .max()
                    .orElse(0) + 1;
        }
        
        rentalRequest.setId(newId);
        rentalRequests.add(rentalRequest);
        
        return saveAllRentalRequests(rentalRequests);
    }

    @Override
    public boolean updateRentalRequest(RentalRequest rentalRequest) {
        List<RentalRequest> rentalRequests = getAllRentalRequests();
        boolean updated = false;
        
        // Replace the rental request with the updated one
        for (int i = 0; i < rentalRequests.size(); i++) {
            if (rentalRequests.get(i).getId() == rentalRequest.getId()) {
                rentalRequests.set(i, rentalRequest);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return saveAllRentalRequests(rentalRequests);
        }
        
        return false;
    }

    @Override
    public boolean deleteRentalRequest(int id) {
        List<RentalRequest> rentalRequests = getAllRentalRequests();
        boolean removed = rentalRequests.removeIf(request -> request.getId() == id);
        
        if (removed) {
            return saveAllRentalRequests(rentalRequests);
        }
        
        return false;
    }
    
    /**
     * Save all rental requests to the file.
     * 
     * @param rentalRequests The list of rental requests to save
     * @return true if the rental requests were saved successfully, false otherwise
     */
    private boolean saveAllRentalRequests(List<RentalRequest> rentalRequests) {
        try {
            // Ensure directory exists
            DatabaseConfig.ensureFileExists(DatabaseConfig.RENTAL_REQUESTS_FILE);
            
            // Write all rental requests to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseConfig.RENTAL_REQUESTS_FILE))) {
                for (RentalRequest rentalRequest : rentalRequests) {
                    writer.write(formatRentalRequestForFile(rentalRequest));
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
     * Format a rental request for file storage.
     * 
     * @param rentalRequest The rental request to format
     * @return The formatted string
     */
    private String formatRentalRequestForFile(RentalRequest rentalRequest) {
        return String.format("%d|%d|%d|%s|%s|%s",
                rentalRequest.getId(),
                rentalRequest.getUserId(),
                rentalRequest.getBikeId(),
                rentalRequest.getRequestTime().format(DATE_TIME_FORMATTER),
                rentalRequest.getRentalType(),
                rentalRequest.getStatus());
    }
}
