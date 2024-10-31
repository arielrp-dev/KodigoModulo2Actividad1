package com.projectmanager.services;

import com.projectmanager.entities.Project;
import com.projectmanager.entities.Task;
import com.projectmanager.entities.User;

import java.io.IOException;
import java.util.List;

public interface Persistable {
    void saveProjects(List<Project> projects) throws IOException;
    void saveTasks(List<Task> tasks) throws IOException;
    void loadProjects(List<Project> projects) throws IOException;
    void loadTasks(List<Task> tasks) throws IOException;
    void loadUsers(List<User> users) throws IOException;
    void saveUsers(List<User> users) throws IOException;
}
