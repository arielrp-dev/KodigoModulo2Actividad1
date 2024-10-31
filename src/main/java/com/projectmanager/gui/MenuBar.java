package com.projectmanager.gui;

import com.projectmanager.services.ProjectManager;

import javax.swing.*;
import java.io.IOException;

public class MenuBar extends JMenuBar {

    public MenuBar(MainFrame mainFrame) {

        // Menú File
        JMenu fileMenu = new JMenu("File");

        // Opciones dentro de File: Add, Update, Delete
        JMenu addMenu = new JMenu("Add");
        JMenuItem addProject = new JMenuItem("Project");
        JMenuItem addTask = new JMenuItem("Task");
        JMenuItem addUser = new JMenuItem("User");

        ProjectManager projectManager = new ProjectManager();

        addProject.addActionListener(e -> {
            try {
                projectManager.addProjectAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        addTask.addActionListener(e -> {
            try {
                projectManager.addTaskAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        addUser.addActionListener(e -> {
            try {
                projectManager.addUserAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        addMenu.add(addProject);
        addMenu.add(addTask);
        addMenu.add(addUser);

        JMenu updateMenu = new JMenu("Update");
        JMenuItem updateProject = new JMenuItem("Project");
        JMenuItem updateTask = new JMenuItem("Task");
        JMenuItem updateUser = new JMenuItem("User");

        updateProject.addActionListener(e -> {
            try {
                projectManager.updateProjectAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateTask.addActionListener(e -> {
            try {
                projectManager.updateTaskAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        updateUser.addActionListener(e -> {
            try {
                projectManager.updateUserAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        updateMenu.add(updateProject);
        updateMenu.add(updateTask);
        updateMenu.add(updateUser);

        JMenu deleteMenu = new JMenu("Delete");
        JMenuItem deleteProject = new JMenuItem("Project");
        JMenuItem deleteTask = new JMenuItem("Task");
        JMenuItem deleteUser = new JMenuItem("User");

        deleteProject.addActionListener(e -> {
            try {
                projectManager.deleteProjectAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        deleteTask.addActionListener(e -> {
            try {
                projectManager.deleteTaskAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        deleteUser.addActionListener(e -> {
            try {
                projectManager.deleteUserAction();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        deleteMenu.add(deleteProject);
        deleteMenu.add(deleteTask);
        deleteMenu.add(deleteUser);

        fileMenu.add(addMenu);
        fileMenu.add(updateMenu);
        fileMenu.add(deleteMenu);

        // Menú View
        JMenu viewMenu = new JMenu("View");

        JMenuItem showProjects = new JMenuItem("Show Projects");
        JMenuItem showTasks = new JMenuItem("Show Tasks");
        JMenuItem showUsers = new JMenuItem("Show Users");
        JMenuItem showTasksByUser = new JMenuItem("Show Task By User");
        JMenuItem showTasksByProject = new JMenuItem("Show Task By Project");

        showProjects.addActionListener(e -> mainFrame.showProjectPanel());
        showTasks.addActionListener(e -> mainFrame.showTaskPanel());
        showUsers.addActionListener(e -> mainFrame.showUserPanel());
        showTasksByUser.addActionListener(e -> {
            try {
                mainFrame.showTaskByUserPanel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        showTasksByProject.addActionListener(e -> {
            try {
                mainFrame.showTaskByProjectPanel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Puedes agregar lógica adicional para "Show Task By User" más tarde
        viewMenu.add(showProjects);
        viewMenu.add(showTasks);
        viewMenu.add(showUsers);
        viewMenu.add(showTasksByUser);
        viewMenu.add(showTasksByProject);

        // Añadir menús a la barra de menú
        this.add(fileMenu);
        this.add(viewMenu);
    }
}