package dao;

import config.DatabaseConfig;
import model.Payment;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * File-based implementation of PaymentDAO.
 */
public class FilePaymentDAO implements PaymentDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        
        // Ensure the file exists
        DatabaseConfig.ensureFileExists(DatabaseConfig.PAYMENTS_FILE);
        
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.PAYMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8) {
                    Payment payment = new Payment(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3]),
                        LocalDateTime.parse(parts[4], DATE_FORMATTER),
                        parts[5],
                        parts[6],
                        parts[7]
                    );
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return payments;
    }

    @Override
    public Payment getPaymentById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.PAYMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 8 && Integer.parseInt(parts[0]) == id) {
                    return new Payment(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]),
                        Double.parseDouble(parts[3]),
                        LocalDateTime.parse(parts[4], DATE_FORMATTER),
                        parts[5],
                        parts[6],
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
    public List<Payment> getPaymentsByRentalId(int rentalId) {
        return getAllPayments().stream()
                .filter(payment -> payment.getRentalId() == rentalId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> getPaymentsByUserId(int userId) {
        return getAllPayments().stream()
                .filter(payment -> payment.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean addPayment(Payment payment) {
        List<Payment> payments = getAllPayments();
        
        // Generate a new ID (get the highest ID and add 1)
        int newId = 1; // Default if no payments exist
        
        if (!payments.isEmpty()) {
            newId = payments.stream()
                    .mapToInt(Payment::getId)
                    .max()
                    .orElse(0) + 1;
        }
        
        payment.setId(newId);
        payments.add(payment);
        
        return saveAllPayments(payments);
    }

    @Override
    public boolean updatePayment(Payment payment) {
        List<Payment> payments = getAllPayments();
        boolean updated = false;
        
        // Replace the payment with the updated one
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId() == payment.getId()) {
                payments.set(i, payment);
                updated = true;
                break;
            }
        }
        
        if (updated) {
            return saveAllPayments(payments);
        }
        
        return false;
    }

    @Override
    public boolean deletePayment(int id) {
        List<Payment> payments = getAllPayments();
        boolean removed = payments.removeIf(payment -> payment.getId() == id);
        
        if (removed) {
            return saveAllPayments(payments);
        }
        
        return false;
    }
    
    /**
     * Save all payments to the file.
     * 
     * @param payments The list of payments to save
     * @return true if the payments were saved successfully, false otherwise
     */
    private boolean saveAllPayments(List<Payment> payments) {
        try {
            // Ensure directory exists
            DatabaseConfig.ensureFileExists(DatabaseConfig.PAYMENTS_FILE);
            
            // Write all payments to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseConfig.PAYMENTS_FILE))) {
                for (Payment payment : payments) {
                    writer.write(formatPaymentForFile(payment));
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
     * Format a payment for file storage.
     * 
     * @param payment The payment to format
     * @return The formatted string
     */
    private String formatPaymentForFile(Payment payment) {
        return String.format("%d|%d|%d|%.2f|%s|%s|%s|%s",
                payment.getId(),
                payment.getRentalId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getPaymentDate().format(DATE_FORMATTER),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId());
    }
}
