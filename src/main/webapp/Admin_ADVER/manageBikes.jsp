<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Bike" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DecimalFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Bikes - Admin Dashboard</title>
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

        // Get bikes from request attribute
        List<Bike> bikes = (List<Bike>) request.getAttribute("bikes");

        // Format for prices
        DecimalFormat df = new DecimalFormat("0.00");

        // Get success and error messages
        String successMessage = (String) request.getAttribute("success");
        String errorMessage = (String) request.getAttribute("error");
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
                        <a href="<%= request.getContextPath() %>/admin/bikes" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg bg-indigo-700 text-white">
                            <i class="fas fa-bicycle mr-3"></i>
                            Manage Bikes
                        </a>
                        <!-- Manage Rental Requests -->
                        <a href="<%= request.getContextPath() %>/admin/rental-requests" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg text-white hover:bg-indigo-700">
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
                        <h1 class="text-lg font-semibold text-gray-900">Manage Bikes</h1>
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

                    <!-- Bikes Management Section -->
                    <div class="bg-white rounded-lg shadow p-6">
                        <div class="flex justify-between items-center mb-6">
                            <h2 class="text-xl font-semibold text-gray-800">All Bikes</h2>
                            <a href="<%= request.getContextPath() %>/admin/bikes/add" class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                <i class="fas fa-plus mr-2"></i> Add New Bike
                            </a>
                        </div>

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
                                        <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white divide-y divide-gray-200">
                                    <% if (bikes != null && !bikes.isEmpty()) {
                                        for (Bike bike : bikes) { %>
                                            <tr>
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= bike.getId() %></td>
                                                <td class="px-6 py-4 whitespace-nowrap">
                                                    <img src="<%= bike.getImageUrl() %>" alt="<%= bike.getName() %>" class="h-12 w-16 object-cover rounded">
                                                </td>
                                                <td class="px-6 py-4 whitespace-nowrap">
                                                    <div class="text-sm font-medium text-gray-900"><%= bike.getName() %></div>
                                                    <div class="text-sm text-gray-500"><%= bike.getDescription().length() > 50 ? bike.getDescription().substring(0, 50) + "..." : bike.getDescription() %></div>
                                                </td>
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
                                                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                                    <div class="flex space-x-2">
                                                        <a href="<%= request.getContextPath() %>/admin/bikes/edit/<%= bike.getId() %>"
                                                           class="text-indigo-600 hover:text-indigo-900" title="Edit">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        <a href="<%= request.getContextPath() %>/admin/bikes/delete/<%= bike.getId() %>"
                                                           class="text-red-600 hover:text-red-900"
                                                           onclick="return confirm('Are you sure you want to delete this bike?')"
                                                           title="Delete">
                                                            <i class="fas fa-trash-alt"></i>
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        <% }
                                    } else { %>
                                        <tr>
                                            <td colspan="8" class="px-6 py-4 text-center text-sm text-gray-500">No bikes found</td>
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
