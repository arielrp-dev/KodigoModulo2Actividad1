package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.projectmanager.entities.Task;
import com.projectmanager.entities.Project;
import com.projectmanager.services.JsonFileManager;

public class TaskByProjectPanel extends JPanel {

    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskByProjectPanel() throws IOException {
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

        // Crear un botón para seleccionar un proyecto
        JButton selectProjectButton = new JButton("Select Project");
        selectProjectButton.addActionListener(e -> selectProject());
        add(selectProjectButton, BorderLayout.SOUTH);

        // Refrescar la lista de tareas
        selectProject();
    }

    // Método para cargar las tareas filtradas en la tabla
    private void loadProjectTasks(List<Task> tasks) {

        // Limpiar la tabla antes de cargar nuevas tareas
        tableModel.setRowCount(0); // Limpiar la tabla antes de cargar nuevas tareas

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

    // Método para seleccionar un proyecto
    private void selectProject() {
        // Crear una instancia de JsonFileManager para manejar archivos JSON
        JsonFileManager jsonFileManager = new JsonFileManager();
        ProjectPanel projectPanel = new ProjectPanel();

        // Lista para almacenar los proyectos cargados desde el archivo JSON
        List<Project> projects = new ArrayList<>();
        try {
            jsonFileManager.loadProjects(projects); // Cargar los proyectos desde el archivo JSON
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error loading projects: " + e.getMessage());
            e.printStackTrace();
        }

        // Verificar si hay proyectos disponibles
        if (projects.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No projects available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear una lista desplegable con los proyectos en el formato "ID - Nombre"
        String[] projectOptions = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            projectOptions[i] = project.getId() + " - " + project.getName();
        }

        // Mostrar un cuadro de diálogo para seleccionar un proyecto
        String selectedProject = (String) JOptionPane.showInputDialog(
                this,
                "Select a project:",
                "Select Project",
                JOptionPane.PLAIN_MESSAGE,
                null,
                projectOptions,
                projectOptions[0] // Seleccionar el primer proyecto por defecto
        );

        // Verificar si el usuario seleccionó uno
        if (selectedProject == null || selectedProject.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No project selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del proyecto seleccionado (el ID está antes del primer guion '-')
        String selectedProjectId = selectedProject.split(" - ")[0];

        // Lista para almacenar las tareas cargadas desde el archivo JSON
        List<Task> tasks = new ArrayList<>();
        try {
            jsonFileManager.loadTasks(tasks); // Cargar todas las tareas desde el archivo JSON
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }

        // Filtrar las tareas que pertenecen al proyecto seleccionado
        List<Task> projectTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getProjectId().equals(selectedProjectId)) {
                projectTasks.add(task);
            }
        }

        // Cargar las tareas asociadas al proyecto en la tabla
        loadProjectTasks(projectTasks);
    }
}
