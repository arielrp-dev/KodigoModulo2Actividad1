package com.projectmanager.entities;

public class Task extends Entity {
    private String description;
    private String status;
    private String projectId;

    public Task(String id, String name, String description, String status, String projectId) {
        super(id, name);
        this.description = description;
        this.status = status;
        this.projectId = projectId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
