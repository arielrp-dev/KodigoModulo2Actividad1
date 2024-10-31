package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.projectmanager.entities.Task;
import com.projectmanager.entities.User;
import com.projectmanager.services.JsonFileManager;

public class TaskByUserPanel extends JPanel {

    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskByUserPanel() {
        // Establecer el layout del panel
        setLayout(new BorderLayout());

        // Crear el modelo de la tabla con las columnas correspondientes
        tableModel = new DefaultTableModel(new Object[]{"Task ID", "Task Name", "Description", "Status", "Project ID"}, 0);

        // Crear la tabla con el modelo
        taskTable = new JTable(tableModel);

        // Aplicar el renderer de estado a la columna del estado
        taskTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Añadir la tabla al panel
        add(scrollPane, BorderLayout.CENTER);

        // Crear un botón para seleccionar un usuario
        JButton selectUserButton = new JButton("Select User");
        selectUserButton.addActionListener(e -> selectUser());
        add(selectUserButton, BorderLayout.SOUTH);

        // Refrescar la lista de tareas
        selectUser();
    }

    // Método para cargar las tareas en la tabla
    private void loadAssignedTasks(List<Task> tasks) {

        // Limpiar la tabla antes de cargar nuevas tareas
        tableModel.setRowCount(0);

        // Recorrer la lista de tareas y agregar cada tarea al modelo de la tabla
        try {
            for (Task task : tasks) {
                // Agregar una fila a la tabla con los datos de la tarea
                tableModel.addRow(new Object[]{task.getId(), task.getName(), task.getDescription(), task.getStatus(), task.getProjectId()});
            }
        }

        // Mostrar un mensaje de error si ocurre algún problema durante la carga
        catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para seleccionar un usuario
    private void selectUser() {
        // Crear una instancia de JsonFileManager para manejar archivos JSON
        JsonFileManager jsonFileManager = new JsonFileManager();
        UserPanel userPanel = new UserPanel();

        // Lista para almacenar los usuarios cargados desde el archivo JSON
        List<User> users = new ArrayList<>();
        try {
            jsonFileManager.loadUsers(users); // Cargar los usuarios desde el archivo JSON
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }

        // Verificar si hay usuarios disponibles
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear una lista desplegable con los usuarios en el formato "ID - Nombre"
        String[] userOptions = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userOptions[i] = user.getId() + " - " + user.getName();
        }

        // Mostrar un cuadro de diálogo para seleccionar un usuario
        String selectedUser = (String) JOptionPane.showInputDialog(
                this,
                "Select a user:",
                "Select User",
                JOptionPane.PLAIN_MESSAGE,
                null,
                userOptions,
                userOptions[0] // Seleccionar el primer usuario por defecto
        );

        // Verificar si el usuario seleccionó uno
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No user selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del usuario seleccionado (el ID está antes del guion '-')
        String selectedUserId = selectedUser.split(" - ")[0];

        // Buscar el usuario seleccionado en la lista de usuarios
        User userToDisplay = users.stream()
                .filter(user -> user.getId().equals(selectedUserId))
                .findFirst()
                .orElse(null);

        if (userToDisplay == null) {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cargar las tareas asociadas al usuario
        List<Task> tasks = new ArrayList<>();
        try {
            jsonFileManager.loadTasks(tasks); // Cargar todas las tareas desde el archivo JSON
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }

        // Filtrar las tareas que están asignadas al usuario seleccionado
        List<Task> assignedTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (userToDisplay.getAssignedTasksIds().contains(task.getId())) {
                assignedTasks.add(task);
            }
        }

        // Cargar las tareas en la tabla
        loadAssignedTasks(assignedTasks);
    }
}
