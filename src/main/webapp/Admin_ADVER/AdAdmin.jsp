<%--
  Created by IntelliJ IDEA.
  User: Venura
  Date: 07/05/2025
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Advertisement" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="service.AdvertisementService" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%
    // Get advertisements from servlet or fetch directly if not using servlet
    List<Advertisement> advertisements = (List<Advertisement>) request.getAttribute("advertisements");
    if (advertisements == null) {
        AdvertisementService service = new AdvertisementService();
        advertisements = service.getAllAdvertisements();
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AdManager Pro</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body class="bg-gray-100 font-sans">
<div class="flex h-screen overflow-hidden">
    <!-- Sidebar -->
    <div class="hidden md:flex md:flex-shrink-0">
        <div class="flex flex-col w-64 bg-indigo-800 text-white">
            <div class="flex items-center justify-center h-16 px-4 bg-indigo-900">
                <span class="text-xl font-bold">AdManager Pro</span>
            </div>
            <div class="flex flex-col flex-grow px-4 py-4 overflow-y-auto">
                <nav class="flex-1 space-y-2">
                    <!-- Dashboard -->
                    <a href="<%= request.getContextPath() %>/advertisements" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg bg-indigo-700 text-white">
                        <i class="fas fa-tachometer-alt mr-3"></i>
                        Dashboard Overview
                    </a>
                    <!-- Manage Bikes -->
                    <a href="<%= request.getContextPath() %>/admin/bikes" class="flex items-center px-4 py-2 text-sm font-medium rounded-lg text-white hover:bg-indigo-700">
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
                        <p class="text-xs text-indigo-200">admin@admanager.com</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="flex flex-col flex-1 overflow-hidden">
        <!-- Top Navigation -->
        <header class="flex items-center justify-between px-6 py-4 bg-white border-b border-gray-200">
            <div class="flex items-center">
                <button class="md:hidden mr-4 text-gray-500 focus:outline-none">
                    <i class="fas fa-bars"></i>
                </button>
                <div class="relative max-w-md w-full">
                    <div class="absolute inset-y-0 left-0 flex items-center pl-3">
                        <i class="fas fa-search text-gray-400"></i>
                    </div>
                    <input class="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm" placeholder="Search..." type="search">
                </div>
            </div>
            <div class="flex items-center space-x-4">
                <button class="p-1 text-gray-400 rounded-full hover:text-gray-500 focus:outline-none">
                    <i class="fas fa-bell"></i>
                </button>
                <button class="p-1 text-gray-400 rounded-full hover:text-gray-500 focus:outline-none">
                    <i class="fas fa-question-circle"></i>
                </button>
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
        </header>

        <!-- Main Content Area -->
        <main class="flex-1 overflow-y-auto p-6 bg-gray-100">
            <div class="mb-6">
                <h1 class="text-2xl font-bold text-gray-800">Dashboard Overview</h1>
                <p class="text-gray-600">Welcome back! Here's what's happening with your ads today.</p>
            </div>

            <!-- Stats Cards -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Active Campaigns</p>
                            <p class="mt-1 text-3xl font-semibold text-gray-900">24</p>
                            <p class="mt-1 text-xs text-green-500">+2 from yesterday</p>
                        </div>
                        <div class="p-3 rounded-full bg-indigo-100 text-indigo-600">
                            <i class="fas fa-bullhorn text-xl"></i>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Impressions (24h)</p>
                            <p class="mt-1 text-3xl font-semibold text-gray-900">1.2M</p>
                            <p class="mt-1 text-xs text-green-500">+12.5% from yesterday</p>
                        </div>
                        <div class="p-3 rounded-full bg-blue-100 text-blue-600">
                            <i class="fas fa-eye text-xl"></i>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Click-through Rate</p>
                            <p class="mt-1 text-3xl font-semibold text-gray-900">2.8%</p>
                            <p class="mt-1 text-xs text-red-500">-0.3% from yesterday</p>
                        </div>
                        <div class="p-3 rounded-full bg-green-100 text-green-600">
                            <i class="fas fa-mouse-pointer text-xl"></i>
                        </div>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow p-6">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Revenue (Today)</p>
                            <p class="mt-1 text-3xl font-semibold text-gray-900">$8,742</p>
                            <p class="mt-1 text-xs text-green-500">+$1,240 from yesterday</p>
                        </div>
                        <div class="p-3 rounded-full bg-purple-100 text-purple-600">
                            <i class="fas fa-dollar-sign text-xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Charts Section -->
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="mb-4">
                        <h2 class="text-lg font-medium text-gray-900">Platform Distribution</h2>
                    </div>
                    <div class="h-64">
                        <canvas id="platformChart"></canvas>
                    </div>
                </div>

                <div class="bg-white rounded-lg shadow p-6">
                    <div class="mb-4">
                        <h2 class="text-lg font-medium text-gray-900">Budget Allocation</h2>
                    </div>
                    <div class="h-64">
                        <canvas id="budgetChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Status Distribution Chart -->
            <div class="grid grid-cols-1 gap-6 mb-6">
                <div class="bg-white rounded-lg shadow p-6">
                    <div class="mb-4">
                        <h2 class="text-lg font-medium text-gray-900">Advertisement Status Distribution</h2>
                    </div>
                    <div class="h-64">
                        <canvas id="statusChart"></canvas>
                    </div>
                </div>
            </div>

            <!-- Recent Activity and Top Campaigns -->
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
                <!-- Recent Activity -->
                <div class="lg:col-span-2 bg-white rounded-lg shadow">
                    <div class="p-6">
                        <h2 class="text-lg font-medium text-gray-900 mb-4">Recent Activity</h2>
                        <div class="space-y-4">
                            <%
                                // Sort advertisements by start date (most recent first)
                                List<Advertisement> recentAds = new ArrayList<>(advertisements);
                                recentAds.sort((a, b) -> b.getStartDate().compareTo(a.getStartDate()));

                                // Take only the first 4 ads for recent activity
                                int activityCount = 0;
                                for (Advertisement ad : recentAds) {
                                    if (activityCount >= 4) break;

                                    String iconClass = "";
                                    String bgColorClass = "";
                                    String textColorClass = "";
                                    String activityTitle = "";
                                    String activityDesc = "";

                                    if (ad.getStatus().equals("Active")) {
                                        iconClass = "fa-check";
                                        bgColorClass = "bg-green-100";
                                        textColorClass = "text-green-600";
                                        activityTitle = "Campaign activated";
                                        activityDesc = "\"" + ad.getTitle() + "\" by " + ad.getAdvertiser() + " is now active";
                                    } else if (ad.getStatus().equals("Paused")) {
                                        iconClass = "fa-pause";
                                        bgColorClass = "bg-yellow-100";
                                        textColorClass = "text-yellow-600";
                                        activityTitle = "Campaign paused";
                                        activityDesc = "\"" + ad.getTitle() + "\" by " + ad.getAdvertiser() + " was paused";
                                    } else if (ad.getStatus().equals("Ended")) {
                                        iconClass = "fa-times";
                                        bgColorClass = "bg-red-100";
                                        textColorClass = "text-red-600";
                                        activityTitle = "Campaign ended";
                                        activityDesc = "\"" + ad.getTitle() + "\" by " + ad.getAdvertiser() + " has ended";
                                    } else {
                                        iconClass = "fa-calendar";
                                        bgColorClass = "bg-blue-100";
                                        textColorClass = "text-blue-600";
                                        activityTitle = "Campaign scheduled";
                                        activityDesc = "\"" + ad.getTitle() + "\" by " + ad.getAdvertiser() + " is scheduled to start";
                                    }

                                    // Calculate relative time (just for display purposes)
                                    String relativeTime = "";
                                    long daysDiff = java.time.temporal.ChronoUnit.DAYS.between(ad.getStartDate(), java.time.LocalDate.now());
                                    if (daysDiff == 0) {
                                        relativeTime = "Today";
                                    } else if (daysDiff == 1) {
                                        relativeTime = "Yesterday";
                                    } else if (daysDiff > 1 && daysDiff < 7) {
                                        relativeTime = daysDiff + " days ago";
                                    } else {
                                        relativeTime = ad.getStartDate().format(formatter);
                                    }
                            %>
                            <div class="flex items-start">
                                <div class="flex-shrink-0 mt-1">
                                    <div class="h-8 w-8 rounded-full <%= bgColorClass %> flex items-center justify-center <%= textColorClass %>">
                                        <i class="fas <%= iconClass %>"></i>
                                    </div>
                                </div>
                                <div class="ml-3">
                                    <p class="text-sm font-medium text-gray-900"><%= activityTitle %></p>
                                    <p class="text-sm text-gray-500"><%= activityDesc %></p>
                                    <p class="text-xs text-gray-400 mt-1"><%= relativeTime %></p>
                                </div>
                            </div>
                            <%
                                    activityCount++;
                                }

                                // If no advertisements, show default message
                                if (activityCount == 0) {
                            %>
                            <div class="flex items-center justify-center py-6">
                                <p class="text-gray-500">No recent activity to display</p>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>

                <!-- Top Campaigns -->
                <div class="bg-white rounded-lg shadow">
                    <div class="p-6">
                        <h2 class="text-lg font-medium text-gray-900 mb-4">Top Performing Campaigns</h2>
                        <div class="space-y-4">
                            <%
                                // Sort advertisements by budget (highest first)
                                List<Advertisement> topAds = new ArrayList<>(advertisements);
                                topAds.sort((a, b) -> Double.compare(b.getBudget(), a.getBudget()));

                                // Take only the first 5 ads for top campaigns
                                int topCount = 0;
                                for (Advertisement ad : topAds) {
                                    if (topCount >= 5) break;

                                    String bgColorClass = "";
                                    String textColorClass = "";
                                    String rankDisplay = "";

                                    if (topCount == 0) {
                                        bgColorClass = "bg-indigo-100";
                                        textColorClass = "text-indigo-600";
                                        rankDisplay = "<i class=\"fas fa-trophy\"></i>";
                                    } else {
                                        switch (topCount) {
                                            case 1:
                                                bgColorClass = "bg-blue-100";
                                                textColorClass = "text-blue-600";
                                                break;
                                            case 2:
                                                bgColorClass = "bg-green-100";
                                                textColorClass = "text-green-600";
                                                break;
                                            case 3:
                                                bgColorClass = "bg-yellow-100";
                                                textColorClass = "text-yellow-600";
                                                break;
                                            case 4:
                                                bgColorClass = "bg-purple-100";
                                                textColorClass = "text-purple-600";
                                                break;
                                        }
                                        rankDisplay = "<span class=\"font-medium\">" + (topCount + 1) + "</span>";
                                    }
                            %>
                            <div class="flex items-center">
                                <div class="flex-shrink-0 h-10 w-10 rounded-full <%= bgColorClass %> flex items-center justify-center <%= textColorClass %>">
                                    <%= rankDisplay %>
                                </div>
                                <div class="ml-3">
                                    <p class="text-sm font-medium text-gray-900"><%= ad.getAdvertiser() %> - <%= ad.getTitle() %></p>
                                    <p class="text-sm text-gray-500">
                                        <% if (ad.getStatus().equals("Active")) { %>
                                            <span class="text-green-600">● Active</span>
                                        <% } else { %>
                                            <span class="text-gray-500">● <%= ad.getStatus() %></span>
                                        <% } %>
                                        • Budget: $<%= String.format("%.2f", ad.getBudget()) %>
                                    </p>
                                </div>
                            </div>
                            <%
                                    topCount++;
                                }

                                // If no advertisements, show default message
                                if (topCount == 0) {
                            %>
                            <div class="flex items-center justify-center py-6">
                                <p class="text-gray-500">No campaigns to display</p>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>

            <!-- All Advertisements -->
            <div class="mt-6 bg-white rounded-lg shadow">
                <div class="p-6">
                    <div class="flex justify-between items-center mb-4">
                        <h2 class="text-lg font-medium text-gray-900">All Advertisements</h2>
                        <a href="<%= request.getContextPath() %>/advertisement/create" class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                            <i class="fas fa-plus mr-2"></i> New Advertisement
                        </a>
                    </div>

                    <%
                        // Advertisements and formatter are already defined at the top of the page
                    %>

                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-gray-200">
                            <thead class="bg-gray-50">
                                <tr>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Title</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Advertiser</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Dates</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Budget</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Platform</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white divide-y divide-gray-200">
                                <% for (Advertisement ad : advertisements) { %>
                                    <tr>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= ad.getId() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <div class="text-sm font-medium text-gray-900"><%= ad.getTitle() %></div>
                                            <div class="text-sm text-gray-500"><%= ad.getDescription() %></div>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= ad.getAdvertiser() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            <%= ad.getStartDate().format(formatter) %> - <%= ad.getEndDate().format(formatter) %>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">$<%= String.format("%.2f", ad.getBudget()) %></td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500"><%= ad.getPlatform() %></td>
                                        <td class="px-6 py-4 whitespace-nowrap">
                                            <% if (ad.getStatus().equals("Active")) { %>
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                                                    <%= ad.getStatus() %>
                                                </span>
                                            <% } else if (ad.getStatus().equals("Paused")) { %>
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-yellow-100 text-yellow-800">
                                                    <%= ad.getStatus() %>
                                                </span>
                                            <% } else if (ad.getStatus().equals("Ended")) { %>
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                                                    <%= ad.getStatus() %>
                                                </span>
                                            <% } else { %>
                                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-gray-100 text-gray-800">
                                                    <%= ad.getStatus() %>
                                                </span>
                                            <% } %>
                                        </td>
                                        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                            <div class="flex space-x-2">
                                                <a href="<%= request.getContextPath() %>/advertisement/edit/<%= ad.getId() %>"
                                                   class="text-indigo-600 hover:text-indigo-900" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="<%= request.getContextPath() %>/advertisement/delete/<%= ad.getId() %>"
                                                   class="text-red-600 hover:text-red-900"
                                                   onclick="return confirm('Are you sure you want to delete this advertisement?')"
                                                   title="Delete">
                                                    <i class="fas fa-trash-alt"></i>
                                                </a>
                                            </div>
                                        </td>
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
    <script>
        // Chart data and configuration
        let adData = [];
        <% for (Advertisement ad : advertisements) { %>
            adData.push({
                id: <%= ad.getId() %>,
                title: "<%= ad.getTitle() %>",
                advertiser: "<%= ad.getAdvertiser() %>",
                startDate: "<%= ad.getStartDate() %>",
                endDate: "<%= ad.getEndDate() %>",
                budget: <%= ad.getBudget() %>,
                platform: "<%= ad.getPlatform() %>",
                status: "<%= ad.getStatus() %>"
            });
        <% } %>

        // Chart colors
        const colors = {
            blue: 'rgba(66, 133, 244, 0.7)',
            green: 'rgba(52, 168, 83, 0.7)',
            yellow: 'rgba(251, 188, 5, 0.7)',
            red: 'rgba(234, 67, 53, 0.7)',
            purple: 'rgba(142, 36, 170, 0.7)',
            orange: 'rgba(255, 152, 0, 0.7)',
            teal: 'rgba(0, 150, 136, 0.7)',
            indigo: 'rgba(63, 81, 181, 0.7)'
        };

        // Initialize charts when DOM is loaded
        document.addEventListener('DOMContentLoaded', function() {
            initPlatformChart();
            initBudgetChart();
            initStatusChart();
        });

        // Platform Distribution Chart
        function initPlatformChart() {
            const ctx = document.getElementById('platformChart').getContext('2d');

            // Count advertisements by platform
            const platformCounts = {};
            adData.forEach(ad => {
                if (!platformCounts[ad.platform]) {
                    platformCounts[ad.platform] = 0;
                }
                platformCounts[ad.platform]++;
            });

            const platforms = Object.keys(platformCounts);
            const counts = platforms.map(platform => platformCounts[platform]);

            // If no data, use sample data
            const chartData = counts.length > 0 ? counts : [4, 3, 2, 1, 1];
            const chartLabels = platforms.length > 0 ? platforms : ['Facebook', 'Instagram', 'Google', 'Twitter', 'YouTube'];

            // Color array
            const colorArray = [
                colors.blue, colors.green, colors.yellow,
                colors.red, colors.purple, colors.orange
            ];

            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: chartLabels,
                    datasets: [{
                        data: chartData,
                        backgroundColor: colorArray,
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right'
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    const value = context.raw;
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = Math.round((value / total) * 100);
                                    return `${context.label}: ${value} (${percentage}%)`;
                                }
                            }
                        }
                    }
                }
            });
        }

        // Budget Allocation Chart
        function initBudgetChart() {
            const ctx = document.getElementById('budgetChart').getContext('2d');

            // Group advertisements by advertiser and sum budgets
            const advertiserBudgets = {};
            adData.forEach(ad => {
                if (!advertiserBudgets[ad.advertiser]) {
                    advertiserBudgets[ad.advertiser] = 0;
                }
                advertiserBudgets[ad.advertiser] += ad.budget;
            });

            const advertisers = Object.keys(advertiserBudgets);
            const budgets = advertisers.map(advertiser => advertiserBudgets[advertiser]);

            // If no data, use sample data
            const chartData = budgets.length > 0 ? budgets : [4000, 3000, 2000, 1800, 1500];
            const chartLabels = advertisers.length > 0 ? advertisers : ['ABC Retail', 'TechNova', 'ElectroMax', 'GiftWorld', 'FreshFoods'];

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: chartLabels,
                    datasets: [{
                        label: 'Budget ($)',
                        data: chartData,
                        backgroundColor: colors.purple,
                        borderColor: colors.purple,
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    indexAxis: 'y',
                    scales: {
                        x: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return '$' + value;
                                }
                            }
                        }
                    },
                    plugins: {
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return 'Budget: $' + context.raw.toFixed(2);
                                }
                            }
                        }
                    }
                }
            });
        }

        // Status Distribution Chart
        function initStatusChart() {
            const ctx = document.getElementById('statusChart').getContext('2d');

            // Count advertisements by status
            const statusCounts = {};
            adData.forEach(ad => {
                if (!statusCounts[ad.status]) {
                    statusCounts[ad.status] = 0;
                }
                statusCounts[ad.status]++;
            });

            const statuses = Object.keys(statusCounts);
            const counts = statuses.map(status => statusCounts[status]);

            // If no data, use sample data
            const chartData = counts.length > 0 ? counts : [5, 2, 1, 1];
            const chartLabels = statuses.length > 0 ? statuses : ['Active', 'Paused', 'Ended', 'Scheduled'];

            // Color mapping for statuses
            const statusColors = {
                'Active': colors.green,
                'Paused': colors.yellow,
                'Ended': colors.red,
                'Scheduled': colors.blue
            };

            // Create color array based on status labels
            const colorArray = chartLabels.map(status =>
                statusColors[status] || colors.indigo
            );

            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: chartLabels,
                    datasets: [{
                        data: chartData,
                        backgroundColor: colorArray,
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right'
                        },
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    const value = context.raw;
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = Math.round((value / total) * 100);
                                    return `${context.label}: ${value} (${percentage}%)`;
                                }
                            }
                        }
                    }
                }
            });
        }
    </script>
</body>
</html>
