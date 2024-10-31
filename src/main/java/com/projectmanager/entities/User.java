package com.projectmanager.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User extends Entity {
    private String email;
    private String phoneNumber;
    private List<String> assignedTasksIds;

    public User(String id, String name, String email, String phoneNumber, List<String> assignedTasksIds) {
        super(id, name);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.assignedTasksIds = assignedTasksIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    public List<String> getAssignedTasksIds() {
        return assignedTasksIds;
    }

    public void setAssignedTasksIds(String[] newAssignedTasksIds) {
        // Convertir el array de String[] a una lista
        List<String> updatedTasksIds = Arrays.asList(newAssignedTasksIds);

        // Asignar la nueva lista de IDs de tareas al usuario
        this.assignedTasksIds = new ArrayList<>(updatedTasksIds);
    }
}
