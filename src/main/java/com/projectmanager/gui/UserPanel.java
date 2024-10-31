package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.projectmanager.entities.User;
import com.projectmanager.services.JsonFileManager;

public class UserPanel extends JPanel {

    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserPanel() {
        // Establecer el layout del panel
        setLayout(new BorderLayout());

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone Number"}, 0);

        // Crear la tabla con el modelo
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Añadir la tabla al panel
        add(scrollPane, BorderLayout.CENTER);

        // Añadir un botón de refrescar al panel
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshUsers());
        add(refreshButton, BorderLayout.SOUTH);

        // Refrescar la lista de usuarios
        refreshUsers();
    }

    // Método para cargar los usuarios en la tabla
    public void loadUsers(List<User> users) {

        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Recorrer la lista de usuarios y agregar cada usuario al modelo de la tabla
        try {
            for (User user : users) {
                // Agregar una fila a la tabla con los datos del usuario
                tableModel.addRow(new Object[]{user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber()});
            }
        }

        // Mostrar un mensaje de error si ocurre algún problema durante la carga
        catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para refrescar la lista de usuarios
    private void refreshUsers() {

        // Mostrar un mensaje en la consola
        System.out.println("Refreshing user list...");

        // Crear una instancia de JsonFileManager para manejar el archivo JSON
        JsonFileManager jsonFileManager = new JsonFileManager();

        // Crear una lista temporal para almacenar las tareas
        List<User> refreshedUsers = new ArrayList<>();

        try {
            // Cargar las tareas desde el archivo JSON
            jsonFileManager.loadUsers(refreshedUsers);

            // Cargar las tareas en el panel (actualizar la tabla)
            loadUsers(refreshedUsers);

            // Mostrar un mensaje de éxito en la consola
            System.out.println("Task list refreshed successfully.");
        } catch (IOException e) {
            // Mostrar un mensaje de error si ocurre algún problema durante la carga
            System.err.println("Error refreshing task list: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
