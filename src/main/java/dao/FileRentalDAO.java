package dao;

import config.DatabaseConfig;
import model.Rental;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File-based implementation of RentalDAO.
 */
public class FileRentalDAO implements RentalDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        
        // Ensure the file exists
        DatabaseConfig.ensureFileExists(DatabaseConfig.RENTALS_FILE);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.RENTALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8) {
                    Rental rental = new Rental(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        LocalDateTime.parse(parts[3], DATE_FORMATTER),
                        parts[4].equals("null") ? null : LocalDateTime.parse(parts[4], DATE_FORMATTER),
                        parts[5],
                        Double.parseDouble(parts[6]),
                        parts[7]
                    );
                    rentals.add(rental);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return rentals;
    }

    @Override
    public Rental getRentalById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.RENTALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8 && Integer.parseInt(parts[0]) == id) {
                    return new Rental(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        LocalDateTime.parse(parts[3], DATE_FORMATTER),
                        parts[4].equals("null") ? null : LocalDateTime.parse(parts[4], DATE_FORMATTER),
                        parts[5],
                        Double.parseDouble(parts[6]),
                        parts[7]
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public List<Rental> getRentalsByUserId(int userId) {
        return getAllRentals().stream()
                .filter(rental -> rental.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> getRentalsByBikeId(int bikeId) {
        return getAllRentals().stream()
                .filter(rental -> rental.getBikeId() == bikeId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> getActiveRentals() {
        return getAllRentals().stream()
                .filter(Rental::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addRental(Rental rental) {
        List<Rental> rentals = getAllRentals();
        
        // Generate a new ID (get the highest ID and add 1)
        int newId = 1; // Default if no rentals exist
        
        if (!rentals.isEmpty()) {
            newId = rentals.stream()
                    .mapToInt(Rental::getId)
                    .max()
                    .orElse(0) + 1;
        }
        
        rental.setId(newId);
        rentals.add(rental);
        
        return saveAllRentals(rentals);
    }

    @Override
    public boolean updateRental(Rental rental) {
        List<Rental> rentals = getAllRentals();
        boolean updated = false;
        
        // Replace the rental with the updated one
        for (int i = 0; i < rentals.size(); i++) {
            if (rentals.get(i).getId() == rental.getId()) {
                rentals.set(i, rental);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return saveAllRentals(rentals);
        }
        
        return false;
    }

    @Override
    public boolean deleteRental(int id) {
        List<Rental> rentals = getAllRentals();
        boolean removed = rentals.removeIf(rental -> rental.getId() == id);
        
        if (removed) {
            return saveAllRentals(rentals);
        }
        
        return false;
    }
    
    /**
     * Save all rentals to the file.
     * 
     * @param rentals The list of rentals to save
     * @return true if the rentals were saved successfully, false otherwise
     */
    private boolean saveAllRentals(List<Rental> rentals) {
        try {
            // Ensure directory exists
            DatabaseConfig.ensureFileExists(DatabaseConfig.RENTALS_FILE);
            
            // Write all rentals to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseConfig.RENTALS_FILE))) {
                for (Rental rental : rentals) {
                    writer.write(formatRentalForFile(rental));
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
     * Format a rental for file storage.
     * 
     * @param rental The rental to format
     * @return The formatted string
     */
    private String formatRentalForFile(Rental rental) {
        String endTimeStr = rental.getEndTime() == null ? "null" : rental.getEndTime().format(DATE_FORMATTER);
        
        return String.format("%d|%d|%d|%s|%s|%s|%.2f|%s",
                rental.getId(),
                rental.getUserId(),
                rental.getBikeId(),
                rental.getStartTime().format(DATE_FORMATTER),
                endTimeStr,
                rental.getRentalType(),
                rental.getTotalCost(),
                rental.getStatus());
    }
}
