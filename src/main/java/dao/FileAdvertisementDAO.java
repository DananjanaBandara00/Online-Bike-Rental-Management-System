package dao;

import config.DatabaseConfig;
import model.Advertisement;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileAdvertisementDAO implements AdvertisementDAO {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<Advertisement> getAllAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();

        // Ensure the file exists
        DatabaseConfig.ensureFileExists(DatabaseConfig.ADVERTISEMENTS_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.ADVERTISEMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 9) {
                    Advertisement ad = new Advertisement(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        LocalDate.parse(parts[3], DATE_FORMATTER),
                        LocalDate.parse(parts[4], DATE_FORMATTER),
                        parts[5],
                        Double.parseDouble(parts[6]),
                        parts[7],
                        parts[8]
                    );
                    advertisements.add(ad);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return advertisements;
    }

    @Override
    public Advertisement getAdvertisementById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DatabaseConfig.ADVERTISEMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 9 && Integer.parseInt(parts[0]) == id) {
                    return new Advertisement(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        LocalDate.parse(parts[3], DATE_FORMATTER),
                        LocalDate.parse(parts[4], DATE_FORMATTER),
                        parts[5],
                        Double.parseDouble(parts[6]),
                        parts[7],
                        parts[8]
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean updateAdvertisement(Advertisement advertisement) {
        List<Advertisement> advertisements = getAllAdvertisements();
        boolean updated = false;

        // Replace the advertisement with the updated one
        for (int i = 0; i < advertisements.size(); i++) {
            if (advertisements.get(i).getId() == advertisement.getId()) {
                advertisements.set(i, advertisement);
                updated = true;
                break;
            }
        }

        if (updated) {
            // Write all advertisements back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseConfig.ADVERTISEMENTS_FILE))) {
                for (Advertisement ad : advertisements) {
                    writer.write(formatAdvertisementForFile(ad));
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean deleteAdvertisement(int id) {
        List<Advertisement> advertisements = getAllAdvertisements();
        boolean removed = advertisements.removeIf(ad -> ad.getId() == id);

        if (removed) {
            // Write all remaining advertisements back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DatabaseConfig.ADVERTISEMENTS_FILE))) {
                for (Advertisement ad : advertisements) {
                    writer.write(formatAdvertisementForFile(ad));
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    // Helper method to format an advertisement for file storage
    private String formatAdvertisementForFile(Advertisement ad) {
        return String.format("%d|%s|%s|%s|%s|%s|%.2f|%s|%s",
            ad.getId(),
            ad.getTitle(),
            ad.getDescription(),
            ad.getStartDate().format(DATE_FORMATTER),
            ad.getEndDate().format(DATE_FORMATTER),
            ad.getAdvertiser(),
            ad.getBudget(),
            ad.getPlatform(),
            ad.getStatus()
        );
    }
}