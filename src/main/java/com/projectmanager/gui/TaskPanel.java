package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.projectmanager.entities.Task;
import com.projectmanager.services.JsonFileManager;

public class TaskPanel extends JPanel {

    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskPanel() {
        // Establecer el layout del panel
        setLayout(new BorderLayout());

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Status", "Project ID"}, 0);

        // Crear la tabla con el modelo
        taskTable = new JTable(tableModel);

        // Aplicar el renderer de estado a la columna del estado
        taskTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Añadir la tabla al panel
        add(scrollPane, BorderLayout.CENTER);

        // Añadir un botón de refrescar al panel
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTasks());
        add(refreshButton, BorderLayout.SOUTH);

        // Refrescar la lista de tareas
        refreshTasks();
    }

    // Método para cargar las tareas en la tabla
    public void loadTasks(List<Task> tasks) {

        // Limpiar la tabla
        tableModel.setRowCount(0); // Limpiar la tabla

        // Recorrer la lista de tareas y agregar cada tarea al modelo de la tabla
        try {
            for (Task task : tasks) {
                // Agregar una fila a la tabla con los datos de la tarea
                tableModel.addRow(new Object[]{task.getId(), task.getName(), task.getDescription(), task.getStatus(), task.getProjectId()});
            }
        }

        // Mostrar un mensaje de error si ocurre algún problema durante la carga
        catch (Exception e) {
            System.err.println("Error loading projects: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para refrescar la lista de tareas
    private void refreshTasks() {

        // Mostrar un mensaje en la consola
        System.out.println("Refreshing task list...");

        // Crear una instancia de JsonFileManager para manejar el archivo JSON
        JsonFileManager jsonFileManager = new JsonFileManager();

        // Crear una lista temporal para almacenar las tareas
        List<Task> refreshedTasks = new ArrayList<>();

        try {
            // Cargar las tareas desde el archivo JSON
            jsonFileManager.loadTasks(refreshedTasks);

            // Cargar las tareas en el panel (actualizar la tabla)
            loadTasks(refreshedTasks);

            // Mostrar un mensaje de éxito en la consola
            System.out.println("Task list refreshed successfully.");
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error refreshing task list: " + e.getMessage());
            e.printStackTrace();
        }
    }
}