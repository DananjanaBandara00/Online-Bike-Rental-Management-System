package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Rental;
import model.RentalRequest;
import model.User;
import service.RentalService;
import service.RentalRequestService;

import java.io.IOException;

@WebServlet("/rent")
public class RentServlet extends HttpServlet {
    private RentalService rentalService;
    private RentalRequestService rentalRequestService;

    @Override
    public void init() {
        rentalService = new RentalService();
        rentalRequestService = new RentalRequestService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get parameters
        String bikeIdStr = request.getParameter("bikeId");
        String rentalType = request.getParameter("rentalType");

        // Validate parameters
        if (bikeIdStr == null || bikeIdStr.isEmpty() || rentalType == null || rentalType.isEmpty()) {
            request.setAttribute("error", "Missing required parameters");
            request.getRequestDispatcher("/bikes").forward(request, response);
            return;
        }

        try {
            int bikeId = Integer.parseInt(bikeIdStr);

            // Create a rental request instead of starting the rental directly
            RentalRequest rentalRequest = rentalRequestService.createRentalRequest(user.getId(), bikeId, rentalType);

            if (rentalRequest != null) {
                // Request successful
                request.setAttribute("success", "Rental request submitted successfully! Your request ID is " + rentalRequest.getId() +
                                     ". An admin will review your request shortly.");
            } else {
                // Request failed
                request.setAttribute("error", "Failed to submit rental request. Please try again later.");
            }

            // Redirect back to bikes page
            request.getRequestDispatcher("/bikes").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid bike ID");
            request.getRequestDispatcher("/bikes").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to the bikes page
        response.sendRedirect(request.getContextPath() + "/bikes");
    }
}
