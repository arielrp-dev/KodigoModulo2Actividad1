package com.projectmanager.gui;

import com.projectmanager.entities.Project;                                                                                     // Se importa la clase Project
import com.projectmanager.services.JsonFileManager;                                                                             // Se importa la clase JsonFileManager
import com.projectmanager.services.ProjectManager;                                                                              // Se importa la clase ProjectManager

import javax.swing.*;                                                                                                           // Se importa la librería swing
import java.awt.*;                                                                                                              // Se importa la librería awt
import java.io.IOException;                                                                                                     // Se importa la librería IOException
import java.util.ArrayList;                                                                                                     // Se importa la librería ArrayList
import java.util.List;                                                                                                          // Se importa la librería List
import java.util.Objects;                                                                                                       // Se importa la librería Objects

public class FormDialogTask extends JDialog {

    private final JTextField[] fields;                                                                                          // Se crea un arreglo de JTextField
    private final JComboBox<String> projectComboBox;                                                                            // Se crea un JComboBox de String
    JComboBox<String> statusComboBox;                                                                                           // Se crea un JComboBox de String
    private boolean confirmed;                                                                                                  // Se crea un booleano

        public FormDialogTask(ProjectManager owner, String title, String[] fieldLabels) throws IOException {                    // Constructor de la clase FormDialogTask
        super();                                                                                                                // Llama al constructor de la clase padre
        setLayout(new BorderLayout());                                                                                          // Establece el layout del formulario
        JsonFileManager jsonFileManager = new JsonFileManager();                                                                // Crea un nuevo JsonFileManager
        List<Project> projects = new ArrayList<>();                                                                             // Crea una nueva lista de proyectos
        jsonFileManager.loadProjects(projects);                                                                                 // Carga los proyectos en la lista
                                                                                                                                // Obtener los nombres de los proyectos
        List<String> projectOptions = new ArrayList<>();                                                                        // Crea una nueva lista de opciones de proyectos
        for (Project project : projects) {                                                                                      // Recorre los proyectos
            projectOptions.add(project.getId() + " - " + project.getName());                                                    // Añade el ID y el nombre del proyecto a la lista
        }
                                                                                                                                // Panel para los campos de entrada
        JPanel formPanel = new JPanel();                                                                                        // Crea un nuevo JPanel
        formPanel.setLayout(new GridLayout(fieldLabels.length + 1, 2));                                                         // Establece el layout del panel
                                                                                                                                // Poner un título al formulario
        setTitle(title);                                                                                                        // Establece el título del formulario
                                                                                                                                // Crear campos de texto
        fields = new JTextField[fieldLabels.length];                                                                            // Inicializa el arreglo de JTextField
        for (int i = 0; i < fieldLabels.length - 2; i++) {                                                                      // Recorre los campos de texto
            formPanel.add(new JLabel(fieldLabels[i]));                                                                          // Añade una etiqueta al panel
            fields[i] = new JTextField(20);                                                                                     // Crea un campo de texto
            formPanel.add(fields[i]);                                                                                           // Añade el campo de texto al panel
        }
                                                                                                                                // Añadir ComboBox para el estado
        formPanel.add(new JLabel(fieldLabels[fieldLabels.length - 2]));                                                         // Añade una etiqueta al panel
        String[] statusOptions = {"Pendiente", "En progreso", "Completado"};                                                    // Opciones para el ComboBox
        statusComboBox = new JComboBox<>(statusOptions);                                                                        // Crea un ComboBox con las opciones
        formPanel.add(statusComboBox);                                                                                          // Añade el ComboBox al panel
                                                                                                                                // Añadir ComboBox para la selección de proyecto
        formPanel.add(new JLabel("Select Project"));                                                                            // Añade una etiqueta al panel
        projectComboBox = new JComboBox<>(projectOptions.toArray(new String[0]));                                               // Crea un ComboBox con las opciones
        formPanel.add(projectComboBox);                                                                                         // Añade el ComboBox al panel
        add(formPanel, BorderLayout.CENTER);                                                                                    // Añade el panel al centro del formulario
                                                                                                                                // Panel para los botones
        JPanel buttonPanel = new JPanel();                                                                                      // Crea un nuevo JPanel
        JButton okButton = new JButton("OK");                                                                                   // Crea un botón de OK
        JButton cancelButton = new JButton("Cancel");                                                                           // Crea un botón de Cancel
                                                                                                                                // Acción del botón OK
        okButton.addActionListener(e -> {                                                                                       // Añade un ActionListener al botón
            confirmed = true;                                                                                                   // Confirma el formulario
            setVisible(false);                                                                                                  // Oculta el formulario
        });
                                                                                                                                // Acción del botón Cancel
        cancelButton.addActionListener(e -> {                                                                                   // Añade un ActionListener al botón
            confirmed = false;                                                                                                  // No confirma el formulario
            setVisible(false);                                                                                                  // Oculta el formulario
        });
                                                                                                                                // Añade los botones al panel
        buttonPanel.add(okButton);                                                                                              // Añade el botón OK al panel
        buttonPanel.add(cancelButton);                                                                                          // Añade el botón Cancel al panel
        add(buttonPanel, BorderLayout.SOUTH);                                                                                   // Añade el panel al sur del formulario
                                                                                                                                // Configuración básica del formulario
        pack();                                                                                                                 // Empaqueta el formulario
        setLocationRelativeTo(owner);                                                                                           // Establece la posición del formulario
    }
                                                                                                                                // Método para obtener los valores ingresados, incluyendo la selección del proyecto
    public String[] getFieldValues() {                                                                                          // Método para obtener los valores ingresados
        String[] values = new String[fields.length];                                                                            // Crea un arreglo de String
        for (int i = 0; i < fields.length - 1; i++) {                                                                           // Recorre los campos de texto
            if (i == fields.length - 2) {                                                                                       // Si es el ComboBox de estado
                values[i] = Objects.requireNonNull(statusComboBox.getSelectedItem()).toString();                                // Obtiene el estado seleccionado
                continue;                                                                                                       // Continúa con la siguiente iteración
            }
            values[i] = fields[i].getText();                                                                                    // Obtiene el texto de cada campo
        }
                                                                                                                                // Separar el ID de projectComboBox.getSelectedItem()
        String IdSelected = Objects.requireNonNull(projectComboBox.getSelectedItem()).toString().split("-")[0].trim();    // Obtiene el ID del proyecto seleccionado
        values[fields.length - 1] = IdSelected;                                                                                 // Añadir la selección del proyecto
        return values;                                                                                                          // Retorna los valores ingresados
    }
                                                                                                                                // Método para verificar si se confirmó el formulario
    public boolean isConfirmed() {                                                                                              // Método para verificar si se confirmó el formulario
        return confirmed;                                                                                                       // Retorna si se confirmó el formulario
    }
                                                                                                                                // Método para prellenar campos (útil para actualizaciones)
    public void setFieldValue(int i, String value) {                                                                            // Método para prellenar campos
        fields[i].setText(value);                                                                                               // Establece el texto del campo
    }
}