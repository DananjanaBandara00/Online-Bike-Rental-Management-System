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

@WebServlet("/advertisement/*")
public class AdvertisementEditServlet extends HttpServlet {
    private AdvertisementService advertisementService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Override
    public void init() {
        advertisementService = new AdvertisementService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        // Path format: /edit/{id} or /delete/{id}
        if (pathInfo != null && pathInfo.length() > 1) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length >= 3) {
                String action = pathParts[1];
                int id;
                
                try {
                    id = Integer.parseInt(pathParts[2]);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid advertisement ID");
                    return;
                }
                
                if ("edit".equals(action)) {
                    // Get the advertisement and forward to edit form
                    Advertisement ad = advertisementService.getAdvertisementById(id);
                    if (ad != null) {
                        request.setAttribute("advertisement", ad);
                        request.getRequestDispatcher("/Admin_ADVER/EditAdvertisement.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Advertisement not found");
                    }
                } else if ("delete".equals(action)) {
                    // Delete the advertisement and redirect to the list
                    boolean deleted = advertisementService.deleteAdvertisement(id);
                    if (deleted) {
                        response.sendRedirect(request.getContextPath() + "/advertisements");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Advertisement not found or could not be deleted");
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle form submission for updating an advertisement
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            LocalDate startDate = LocalDate.parse(request.getParameter("startDate"), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(request.getParameter("endDate"), DATE_FORMATTER);
            String advertiser = request.getParameter("advertiser");
            double budget = Double.parseDouble(request.getParameter("budget"));
            String platform = request.getParameter("platform");
            String status = request.getParameter("status");
            
            Advertisement ad = new Advertisement(id, title, description, startDate, endDate, advertiser, budget, platform, status);
            
            boolean updated = advertisementService.updateAdvertisement(ad);
            if (updated) {
                response.sendRedirect(request.getContextPath() + "/advertisements");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update advertisement");
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
