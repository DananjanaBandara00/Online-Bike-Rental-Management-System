package service;

import dao.AdvertisementDAO;
import dao.FileAdvertisementDAO;
import model.Advertisement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AdvertisementService {
    private AdvertisementDAO advertisementDAO;

    public AdvertisementService() {
        this.advertisementDAO = new FileAdvertisementDAO();
    }

    public List<Advertisement> getAllAdvertisements() {
        return advertisementDAO.getAllAdvertisements();
    }

    public Advertisement getAdvertisementById(int id) {
        return advertisementDAO.getAdvertisementById(id);
    }

    public List<Advertisement> getActiveAdvertisements() {
        LocalDate today = LocalDate.now();
        return getAllAdvertisements().stream()
                .filter(ad -> "Active".equals(ad.getStatus()))
                .filter(ad -> !ad.getEndDate().isBefore(today))
                .collect(Collectors.toList());
    }

    public boolean updateAdvertisement(Advertisement advertisement) {
        return advertisementDAO.updateAdvertisement(advertisement);
    }

    public boolean deleteAdvertisement(int id) {
        return advertisementDAO.deleteAdvertisement(id);
    }
}
