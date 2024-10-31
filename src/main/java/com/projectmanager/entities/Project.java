package com.projectmanager.entities;

public class Project extends Entity {
    private String description;
    private String status;

    public Project(String id, String name, String description, String status) {
        super(id, name);
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}