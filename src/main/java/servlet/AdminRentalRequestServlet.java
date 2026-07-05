package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Bike;
import model.RentalRequest;
import model.User;
import service.BikeService;
import service.RentalRequestService;
import service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/admin/rental-requests", "/admin/rental-requests/approve/*", "/admin/rental-requests/reject/*"})
public class AdminRentalRequestServlet extends HttpServlet {
    private RentalRequestService rentalRequestService;
    private BikeService bikeService;
    private UserService userService;

    @Override
    public void init() {
        rentalRequestService = new RentalRequestService();
        bikeService = new BikeService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is logged in and is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!user.isAdmin() && !user.getEmail().toLowerCase().endsWith("@admin.com")) {
            response.sendRedirect(request.getContextPath() + "/userDashboard.jsp");
            return;
        }

        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();

        if (path.equals("/admin/rental-requests") && pathInfo == null) {
            // List all rental requests
            List<RentalRequest> rentalRequests = rentalRequestService.getAllRentalRequests();

            // Get bikes and users for display
            Map<Integer, Bike> bikeMap = new HashMap<>();
            Map<Integer, User> userMap = new HashMap<>();

            for (RentalRequest rentalRequest : rentalRequests) {
                // Get bike if not already in map
                if (!bikeMap.containsKey(rentalRequest.getBikeId())) {
                    Bike bike = bikeService.getBikeById(rentalRequest.getBikeId());
                    if (bike != null) {
                        bikeMap.put(bike.getId(), bike);
                    }
                }

                // Get user if not already in map
                if (!userMap.containsKey(rentalRequest.getUserId())) {
                    User requestUser = userService.getUserById(rentalRequest.getUserId());
                    if (requestUser != null) {
                        userMap.put(requestUser.getId(), requestUser);
                    }
                }
            }

            // Sort bikes by availability
            List<Bike> sortedBikes = rentalRequestService.sortBikesByAvailability(bikeService.getAllBikes());

            request.setAttribute("rentalRequests", rentalRequests);
            request.setAttribute("bikeMap", bikeMap);
            request.setAttribute("userMap", userMap);
            request.setAttribute("sortedBikes", sortedBikes);

            request.getRequestDispatcher("/Admin_ADVER/manageRentalRequests.jsp").forward(request, response);
        } else if (path.equals("/admin/rental-requests/approve") && pathInfo != null) {
            // Approve rental request
            try {
                int requestId = Integer.parseInt(pathInfo.substring(1)); // Remove the leading slash
                boolean approved = rentalRequestService.approveRentalRequest(requestId);

                if (approved) {
                    session.setAttribute("success", "Rental request approved successfully");
                } else {
                    session.setAttribute("error", "Failed to approve rental request");
                }

                response.sendRedirect(request.getContextPath() + "/admin/rental-requests");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/rental-requests");
            }
        } else if (path.equals("/admin/rental-requests/reject") && pathInfo != null) {
            // Reject rental request
            try {
                int requestId = Integer.parseInt(pathInfo.substring(1)); // Remove the leading slash
                boolean rejected = rentalRequestService.rejectRentalRequest(requestId);

                if (rejected) {
                    session.setAttribute("success", "Rental request rejected successfully");
                } else {
                    session.setAttribute("error", "Failed to reject rental request");
                }

                response.sendRedirect(request.getContextPath() + "/admin/rental-requests");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/admin/rental-requests");
            }
        } else {
            // Invalid path
            response.sendRedirect(request.getContextPath() + "/admin/rental-requests");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect POST requests to GET
        doGet(request, response);
    }
}
