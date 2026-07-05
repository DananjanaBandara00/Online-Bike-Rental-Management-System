<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Bike" %>
<%@ page import="model.RentalRequest" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Rental Requests - Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-100 font-sans">
    <%
        // Check if user is logged in and is admin
        User user = (User) session.getAttribute("user");
        if (user == null || (!user.isAdmin() && !user.getEmail().toLowerCase().endsWith("@admin.com"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get data from request attributes
        List<RentalRequest> rentalRequests = (List<RentalRequest>) request.getAttribute("rentalRequests");
        Map<Integer, Bike> bikeMap = (Map<Integer, Bike>) request.getAttribute("bikeMap");
        Map<Integer, User> userMap = (Map<Integer, User>) request.getAttribute("userMap");
        List<Bike> sortedBikes = (List<Bike>) request.getAttribute("sortedBikes");

        // Format for dates and times
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format for prices
        DecimalFormat df = new DecimalFormat("0.00");

        // Get success and error messages from session
        String successMessage = (String) session.getAttribute("success");
        String errorMessage = (String) session.getAttribute("error");

        // Remove messages from session after retrieving them
        if (successMessage != null) {
            session.removeAttribute("success");
        }
        if (errorMessage != null) {
            session.removeAttribute("error");
        }
    %>

    <div class="flex h-screen overflow-hidden">
        <!-- Sidebar -->
        <div class="hidden md:flex md:flex-shrink-0">
            <div class="flex flex-col w-64 bg-indigo-800 text-white">
                <div class="flex items-center justify-center h-16 px-4 bg-indigo-900">
                    <span class="text-xl font-bold">Admin Dashboard</span>
                </div>
                <div class="flex flex-col flex-grow px-4 py-4 overflow-y-auto">
                    <nav class="flex-1 space-y-2">
                        <!-- Dashboard -->
                        <a href="<%= request.getContextPath() %>/advertisements" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg text-white hover:bg-indigo-700">
                            <i class="fas fa-tachometer-alt mr-3"></i>
                            Dashboard
                        </a>
                        <!-- Manage Bikes -->
                        <a href="<%= request.getContextPath() %>/admin/bikes" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg text-white hover:bg-indigo-700">
                            <i class="fas fa-bicycle mr-3"></i>
                            Manage Bikes
                        </a>
                        <!-- Manage Rental Requests -->
                        <a href="<%= request.getContextPath() %>/admin/rental-requests" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg bg-indigo-700 text-white">
                            <i class="fas fa-clipboard-list mr-3"></i>
                            Rental Requests
                        </a>
                        <!-- Logout -->
                        <a href="<%= request.getContextPath() %>/logout" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg text-white hover:bg-indigo-700">
                            <i class="fas fa-sign-out-alt mr-3"></i>
                            Logout
                        </a>
                    </nav>
                </div>
                <div class="p-4 border-t border-indigo-700">
                    <div class="flex items-center">
                        <div class="w-10 h-10 rounded-full bg-indigo-300 text-indigo-800 flex items-center justify-center font-medium">
                            A
                        </div>
                        <div class="ml-3">
                            <p class="text-sm font-medium">Admin User</p>
                            <p class="text-xs text-indigo-200"><%= user.getEmail() %></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <div class="flex flex-col flex-1 overflow-hidden">
            <!-- Top Navigation -->
            <header class="bg-white shadow-sm z-10">
                <div class="px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
                    <div class="flex items-center">
                        <button class="md:hidden mr-4 text-gray-500 focus:outline-none">
                            <i class="fas fa-bars"></i>
                        </button>
                        <h1 class="text-lg font-semibold text-gray-900">Manage Rental Requests</h1>
                    </div>
                    <div class="flex items-center space-x-4">
                        <div class="relative">
                            <button class="flex items-center focus:outline-none">
                                <span class="hidden md:block mr-2 text-sm font-medium">Admin User</span>
                                <div class="w-8 h-8 rounded-full bg-indigo-300 text-indigo-800 flex items-center justify-center font-medium">
                                    A
                                </div>
                            </button>
                        </div>
                        <a href="<%= request.getContextPath() %>/logout" class="px-3 py-1 bg-red-600 text-white rounded-md text-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500">
                            <i class="fas fa-sign-out-alt mr-1"></i> Logout
                        </a>
                    </div>
                </div>
            </header>

            <!-- Page Content -->
            <main class="flex-1 overflow-y-auto p-4 sm:p-6 lg:p-8 bg-gray-100">
                <div class="max-w-7xl mx-auto">
                    <!-- Success/Error Messages -->
                    <% if (successMessage != null && !successMessage.isEmpty()) { %>
                        <div class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4" role="alert">
                            <span class="block sm:inline"><%= successMessage %></span>
                        </div>
                    <% } %>
                    <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
                        <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4" role="alert">
                            <span class="block sm:inline"><%= errorMessage %></span>
                        </div>
                    <% } %>

                    <!-- Rental Requests Section -->
                    <div class="bg-white rounded-lg shadow p-6 mb-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">Rental Requests</h2>

                        <!-- Rental Requests Table -->
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">User</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Bike</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Request Time</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Rental Type</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% if (rentalRequests != null && !rentalRequests.isEmpty()) {
                                        for (RentalRequest rentalReq : rentalRequests) {
                                            Bike bike = bikeMap.get(rentalReq.getBikeId());
                                            User requestUser = userMap.get(rentalReq.getUserId());

                                            if (bike == null || requestUser == null) {
                                                continue;
                                            }
                                    %>
                                        <tr>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= rentalReq.getId() %></td>
                                            <td class="px-6 py-4 whitespace-nowrap">
                                                <div class="flex items-center">
                                                    <div class="w-8 h-8 rounded-full bg-indigo-300 text-indigo-800 flex items-center justify-center font-medium">
                                                        <%= requestUser.getName().substring(0, 1).toUpperCase() %>
                                                    </div>
                                                    <div class="ml-3">
                                                        <div class="text-sm font-medium text-gray-900"><%= requestUser.getName() %></div>
                                                        <div class="text-sm text-gray-500"><%= requestUser.getEmail() %></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap">
                                                <div class="flex items-center">
                                                    <img src="<%= bike.getImageUrl() %>" alt="<%= bike.getName() %>" class="h-8 w-12 object-cover rounded">
                                                    <div class="ml-3">
                                                        <div class="text-sm font-medium text-gray-900"><%= bike.getName() %></div>
                                                        <div class="text-sm text-gray-500"><%= bike.getType() %></div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                <%= rentalReq.getRequestTime().format(dateTimeFormatter) %>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                <%= rentalReq.getRentalType() %>
                                                <% if (rentalReq.getRentalType().equals("Hourly")) { %>
                                                    ($<%= df.format(bike.getHourlyRate()) %>/hour)
                                                <% } else { %>
                                                    ($<%= df.format(bike.getDailyRate()) %>/day)
                                                <% } %>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap">
                                                <% if (rentalReq.getStatus().equals("Pending")) { %>
                                                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                                                        <%= rentalReq.getStatus() %>
                                                    </span>
                                                <% } else if (rentalReq.getStatus().equals("Approved")) { %>
                                                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                        <%= rentalReq.getStatus() %>
                                                    </span>
                                                <% } else if (rentalReq.getStatus().equals("Rejected")) { %>
                                                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                        <%= rentalReq.getStatus() %>
                                                    </span>
                                                <% } %>
                                            </td>
                                            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                <% if (rentalReq.getStatus().equals("Pending")) { %>
                                                    <div class="flex space-x-2">
                                                        <a href="<%= request.getContextPath() %>/admin/rental-requests/approve/<%= rentalReq.getId() %>"
                                                           class="text-green-600 hover:text-green-900" title="Approve">
                                                            <i class="fas fa-check"></i>
                                                        </a>
                                                        <a href="<%= request.getContextPath() %>/admin/rental-requests/reject/<%= rentalReq.getId() %>"
                                                           class="text-red-600 hover:text-red-900" title="Reject">
                                                            <i class="fas fa-times"></i>
                                                        </a>
                                                    </div>
                                                <% } else { %>
                                                    <span class="text-gray-400">No actions available</span>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <% }
                                    } else { %>
                                        <tr>
                                            <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">No rental requests found</td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Bikes Availability Section -->
                    <div class="bg-white rounded-lg shadow p-6">
                        <h2 class="text-xl font-semibold text-gray-800 mb-6">Bikes Availability (Sorted)</h2>

                        <!-- Bikes Table -->
                        <div class="overflow-x-auto">
                            <table class="min-w-full divide-y divide-gray-200">
                                <thead class="bg-gray-50">
                                    <tr>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Image</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Hourly Rate</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Daily Rate</th>
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% if (sortedBikes != null && !sortedBikes.isEmpty()) {
                                        for (Bike bike : sortedBikes) { %>
                                            <tr>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= bike.getId() %></td>
                                                <td class="px-6 py-4 whitespace-nowrap">
                                                    <img src="<%= bike.getImageUrl() %>" alt="<%= bike.getName() %>" class="h-8 w-12 object-cover rounded">
                                                </td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"><%= bike.getName() %></td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= bike.getType() %></td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$<%= df.format(bike.getHourlyRate()) %></td>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$<%= df.format(bike.getDailyRate()) %></td>
                                                <td class="px-6 py-4 whitespace-nowrap">
                                                    <% if (bike.getStatus().equals("Available")) { %>
                                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                            <%= bike.getStatus() %>
                                                        </span>
                                                    <% } else if (bike.getStatus().equals("Rented")) { %>
                                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                                                            <%= bike.getStatus() %>
                                                        </span>
                                                    <% } else if (bike.getStatus().equals("Maintenance")) { %>
                                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                            <%= bike.getStatus() %>
                                                        </span>
                                                    <% } else { %>
                                                        <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                                                            <%= bike.getStatus() %>
                                                        </span>
                                                    <% } %>
                                                </td>
                                            </tr>
                                        <% }
                                    } else { %>
                                        <tr>
                                            <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">No bikes found</td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
</body>
</html>
