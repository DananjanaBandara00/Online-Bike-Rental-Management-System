package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Advertisement;
import service.AdvertisementService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/advertisement/create")
public class AdvertisementCreateServlet extends HttpServlet {
    private AdvertisementService advertisementService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public void init() {
        advertisementService = new AdvertisementService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to the create advertisement form
        request.getRequestDispatcher("/Admin_ADVER/CreateAdvertisement.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle form submission for creating a new advertisement
        try {
            // Generate a new ID (get the highest ID and add 1)
            List<Advertisement> allAds = advertisementService.getAllAdvertisements();
            int newId = 1; // Default if no ads exist
            
            if (!allAds.isEmpty()) {
                newId = allAds.stream()
                        .mapToInt(Advertisement::getId)
                        .max()
                        .orElse(0) + 1;
            }
            
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            LocalDate startDate = LocalDate.parse(request.getParameter("startDate"), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(request.getParameter("endDate"), DATE_FORMATTER);
            String advertiser = request.getParameter("advertiser");
            double budget = Double.parseDouble(request.getParameter("budget"));
            String platform = request.getParameter("platform");
            String status = request.getParameter("status");
            
            Advertisement ad = new Advertisement(newId, title, description, startDate, endDate, advertiser, budget, platform, status);
            
            boolean created = advertisementService.updateAdvertisement(ad); // We can use the update method to create as well
            if (created) {
                response.sendRedirect(request.getContextPath() + "/advertisements");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create advertisement");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format: " + e.getMessage());
        } catch (DateTimeParseException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}
