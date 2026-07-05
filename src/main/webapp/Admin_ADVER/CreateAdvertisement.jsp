<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Advertisement - AdManager Pro</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body class="bg-gray-100 font-sans">
    <%
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);
    %>

    <div class="min-h-screen flex flex-col">
        <!-- Header -->
        <header class="bg-indigo-800 text-white py-4">
            <div class="container mx-auto px-4">
                <div class="flex justify-between items-center">
                    <h1 class="text-2xl font-bold">AdManager Pro</h1>
                    <div class="flex items-center space-x-4">
                        <a href="<%= request.getContextPath() %>/advertisements" class="text-white hover:text-indigo-200">
                            <i class="fas fa-arrow-left mr-2"></i> Back to Dashboard
                        </a>
                        <a href="<%= request.getContextPath() %>/logout" class="text-white hover:text-indigo-200">
                            <i class="fas fa-sign-out-alt mr-2"></i> Logout
                        </a>
                    </div>
                </div>
            </div>
        </header>

        <!-- Main Content -->
        <main class="flex-grow container mx-auto px-4 py-8">
            <div class="bg-white rounded-lg shadow-md p-6 max-w-4xl mx-auto">
                <h2 class="text-2xl font-bold text-gray-800 mb-6">Create New Advertisement</h2>

                <form action="<%= request.getContextPath() %>/advertisement/create" method="post" class="space-y-6">
                    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <!-- Title -->
                        <div>
                            <label for="title" class="block text-sm font-medium text-gray-700 mb-1">Title</label>
                            <input type="text" id="title" name="title"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                        </div>

                        <!-- Advertiser -->
                        <div>
                            <label for="advertiser" class="block text-sm font-medium text-gray-700 mb-1">Advertiser</label>
                            <input type="text" id="advertiser" name="advertiser"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                        </div>

                        <!-- Start Date -->
                        <div>
                            <label for="startDate" class="block text-sm font-medium text-gray-700 mb-1">Start Date</label>
                            <input type="date" id="startDate" name="startDate" value="<%= today.format(formatter) %>"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                        </div>

                        <!-- End Date -->
                        <div>
                            <label for="endDate" class="block text-sm font-medium text-gray-700 mb-1">End Date</label>
                            <input type="date" id="endDate" name="endDate" value="<%= oneMonthLater.format(formatter) %>"
                                   class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                        </div>

                        <!-- Budget -->
                        <div>
                            <label for="budget" class="block text-sm font-medium text-gray-700 mb-1">Budget</label>
                            <div class="relative">
                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <span class="text-gray-500">$</span>
                                </div>
                                <input type="number" id="budget" name="budget" value="1000.00" step="0.01" min="0"
                                       class="w-full pl-7 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                            </div>
                        </div>

                        <!-- Platform -->
                        <div>
                            <label for="platform" class="block text-sm font-medium text-gray-700 mb-1">Platform</label>
                            <select id="platform" name="platform"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                                <option value="Facebook">Facebook</option>
                                <option value="Instagram">Instagram</option>
                                <option value="Google">Google</option>
                                <option value="Twitter">Twitter</option>
                                <option value="YouTube">YouTube</option>
                                <option value="LinkedIn">LinkedIn</option>
                            </select>
                        </div>

                        <!-- Status -->
                        <div>
                            <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Status</label>
                            <select id="status" name="status"
                                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required>
                                <option value="Active">Active</option>
                                <option value="Paused">Paused</option>
                                <option value="Scheduled">Scheduled</option>
                            </select>
                        </div>
                    </div>

                    <!-- Description -->
                    <div>
                        <label for="description" class="block text-sm font-medium text-gray-700 mb-1">Description</label>
                        <textarea id="description" name="description" rows="4"
                                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500" required></textarea>
                    </div>

                    <!-- Submit Button -->
                    <div class="flex justify-end space-x-4">
                        <a href="<%= request.getContextPath() %>/advertisements"
                           class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                            Cancel
                        </a>
                        <button type="submit"
                                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                            Create Advertisement
                        </button>
                    </div>
                </form>
            </div>
        </main>

        <!-- Footer -->
        <footer class="bg-white border-t border-gray-200 py-4">
            <div class="container mx-auto px-4">
                <p class="text-center text-gray-500 text-sm">© 2025 AdManager Pro. All rights reserved.</p>
            </div>
        </footer>
    </div>
</body>
</html>
