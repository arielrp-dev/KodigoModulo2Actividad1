package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.projectmanager.entities.Project;
import com.projectmanager.services.JsonFileManager;

public class ProjectPanel extends JPanel {

    private JTable projectTable;
    private DefaultTableModel tableModel;

    public ProjectPanel() {
        // Establecer el layout del panel
        setLayout(new BorderLayout());

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Status"}, 0);

        // Crear la tabla con el modelo
        projectTable = new JTable(tableModel); 
        JScrollPane scrollPane = new JScrollPane(projectTable);

        // Aplicar el renderer de estado a la columna del estado
        projectTable.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        // Añadir la tabla al panel
        add(scrollPane, BorderLayout.CENTER);

        // Añadir un botón de refrescar al panel
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshProjects());
        add(refreshButton, BorderLayout.SOUTH);

        // Refrescar la lista de proyectos
        refreshProjects();
    }

    // Método para cargar los proyectos en la tabla
    public void loadProjects(List<Project> projects) {

        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Recorrer la lista de tareas y agregar cada tarea al modelo de la tabla
        try {
            for (Project project : projects) {
                // Agregar una fila a la tabla con los datos de la tarea
                tableModel.addRow(new Object[]{project.getId(), project.getName(), project.getDescription(), project.getStatus()});
            }
        }

        // Mostrar un mensaje de error si ocurre algún problema durante la carga
        catch (Exception e) {
            System.err.println("Error loading projects: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para refrescar la lista de proyectos
    public void refreshProjects() {

        // Mostrar un mensaje en la consola
        System.out.println("Refreshing task list...");

        // Crear una instancia de JsonFileManager para manejar el archivo JSON
        JsonFileManager jsonFileManager = new JsonFileManager();

        // Crear una lista temporal para almacenar las tareas
        List<Project> refreshedProjects = new ArrayList<>();

        try {
            // Cargar las tareas desde el archivo JSON
            jsonFileManager.loadProjects(refreshedProjects);

            // Cargar las tareas en el panel (actualizar la tabla)
            loadProjects(refreshedProjects);

            // Mostrar un mensaje de éxito en la consola
            System.out.println("Task list refreshed successfully.");
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error refreshing task list: " + e.getMessage());
            e.printStackTrace();
        }
    }
}