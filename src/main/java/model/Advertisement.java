package model;

import java.time.LocalDate;

public class Advertisement {
    private int id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String advertiser;
    private double budget;
    private String platform;
    private String status;

    // Constructor
    public Advertisement(int id, String title, String description, LocalDate startDate,
                        LocalDate endDate, String advertiser, double budget, String platform, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.advertiser = advertiser;
        this.budget = budget;
        this.platform = platform;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getAdvertiser() { return advertiser; }
    public void setAdvertiser(String advertiser) { this.advertiser = advertiser; }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}