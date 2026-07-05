<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Bike" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Bike - Admin Dashboard</title>
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
        
        // Get bike from request attribute
        Bike bike = (Bike) request.getAttribute("bike");
        if (bike == null) {
            response.sendRedirect(request.getContextPath() + "/admin/bikes");
            return;
        }
        
        // Get error message if any
        String errorMessage = (String) request.getAttribute("error");
    %>

    <div class="min-h-screen bg-gray-100">
        <div class="bg-indigo-800 text-white py-4">
            <div class="container mx-auto px-4 flex justify-between items-center">
                <h1 class="text-2xl font-bold">Admin Dashboard</h1>
                <div class="flex items-center space-x-4">
                    <a href="<%= request.getContextPath() %>/admin/bikes" class="text-white hover:text-indigo-200">
                        <i class="fas fa-arrow-left mr-2"></i> Back to Bikes
                    </a>
                    <a href="<%= request.getContextPath() %>/logout" class="text-white hover:text-indigo-200">
                        <i class="fas fa-sign-out-alt mr-2"></i> Logout
                    </a>
                </div>
            </div>
        </div>

        <div class="container mx-auto px-4 py-8">
            <div class="max-w-3xl mx-auto bg-white rounded-lg shadow-md overflow-hidden">
                <div class="px-6 py-4 bg-indigo-600 text-white">
                    <h2 class="text-xl font-semibold">Edit Bike: <%= bike.getName() %></h2>
                </div>
                
                <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
                    <div class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 m-4 rounded" role="alert">
                        <span class="block sm:inline"><%= errorMessage %></span>
                    </div>
                <% } %>
                
                <div class="p-6">
                    <form action="<%= request.getContextPath() %>/admin/bikes/edit/<%= bike.getId() %>" method="post" class="space-y-6">
                        <!-- Bike Name -->
                        <div>
                            <label for="name" class="block text-sm font-medium text-gray-700 mb-1">Bike Name</label>
                            <input type="text" id="name" name="name" value="<%= bike.getName() %>"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                   required>
                        </div>
                        
                        <!-- Bike Type -->
                        <div>
                            <label for="type" class="block text-sm font-medium text-gray-700 mb-1">Bike Type</label>
                            <select id="type" name="type" 
                                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                    required>
                                <option value="Mountain" <%= "Mountain".equals(bike.getType()) ? "selected" : "" %>>Mountain</option>
                                <option value="Road" <%= "Road".equals(bike.getType()) ? "selected" : "" %>>Road</option>
                                <option value="City" <%= "City".equals(bike.getType()) ? "selected" : "" %>>City</option>
                                <option value="BMX" <%= "BMX".equals(bike.getType()) ? "selected" : "" %>>BMX</option>
                                <option value="Electric" <%= "Electric".equals(bike.getType()) ? "selected" : "" %>>Electric</option>
                                <option value="Hybrid" <%= "Hybrid".equals(bike.getType()) ? "selected" : "" %>>Hybrid</option>
                                <option value="Kids" <%= "Kids".equals(bike.getType()) ? "selected" : "" %>>Kids</option>
                                <option value="Tandem" <%= "Tandem".equals(bike.getType()) ? "selected" : "" %>>Tandem</option>
                            </select>
                        </div>
                        
                        <!-- Description -->
                        <div>
                            <label for="description" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                            <textarea id="description" name="description" rows="3" 
                                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                      required><%= bike.getDescription() %></textarea>
                        </div>
                        
                        <!-- Rates -->
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label for="hourlyRate" class="block text-sm font-medium text-gray-700 mb-1">Hourly Rate ($)</label>
                                <input type="number" id="hourlyRate" name="hourlyRate" step="0.01" min="0" value="<%= bike.getHourlyRate() %>"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                       required>
                            </div>
                            <div>
                                <label for="dailyRate" class="block text-sm font-medium text-gray-700 mb-1">Daily Rate ($)</label>
                                <input type="number" id="dailyRate" name="dailyRate" step="0.01" min="0" value="<%= bike.getDailyRate() %>"
                                       class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                       required>
                            </div>
                        </div>
                        
                        <!-- Status -->
                        <div>
                            <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                            <select id="status" name="status" 
                                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                    required>
                                <option value="Available" <%= "Available".equals(bike.getStatus()) ? "selected" : "" %>>Available</option>
                                <option value="Rented" <%= "Rented".equals(bike.getStatus()) ? "selected" : "" %>>Rented</option>
                                <option value="Maintenance" <%= "Maintenance".equals(bike.getStatus()) ? "selected" : "" %>>Maintenance</option>
                            </select>
                        </div>
                        
                        <!-- Image URL -->
                        <div>
                            <label for="imageUrl" class="block text-sm font-medium text-gray-700 mb-1">Image URL</label>
                            <input type="url" id="imageUrl" name="imageUrl" value="<%= bike.getImageUrl() %>"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" 
                                   required>
                            <p class="mt-1 text-sm text-gray-500">Enter a URL for the bike image (e.g., https://example.com/bike.jpg)</p>
                        </div>
                        
                        <!-- Current Image Preview -->
                        <div>
                            <label class="block text-sm font-medium text-gray-700 mb-1">Current Image</label>
                            <img src="<%= bike.getImageUrl() %>" alt="<%= bike.getName() %>" class="h-40 object-cover rounded border border-gray-300">
                        </div>
                        
                        <!-- Submit Button -->
                        <div class="flex justify-end">
                            <button type="submit" 
                                    class="px-4 py-2 border border-transparent rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                                Update Bike
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
