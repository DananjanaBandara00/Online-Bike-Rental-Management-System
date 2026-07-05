package dao;

import model.Advertisement;
import java.util.List;

public interface AdvertisementDAO {
    List<Advertisement> getAllAdvertisements();
    Advertisement getAdvertisementById(int id);
    boolean updateAdvertisement(Advertisement advertisement);
    boolean deleteAdvertisement(int id);
}