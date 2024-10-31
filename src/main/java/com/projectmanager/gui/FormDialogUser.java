package com.projectmanager.gui;

import com.projectmanager.entities.Task;                                                                        // importar la clase Task
import com.projectmanager.services.JsonFileManager;                                                             // importar la clase JsonFileManager
import com.projectmanager.services.ProjectManager;                                                              // importar la clase ProjectManager

import javax.swing.*;                                                                                           // importar la clase javax.swing.JFrame
import java.awt.*;                                                                                              // importar la clase java.awt.BorderLayout
import java.io.IOException;                                                                                     // importar la clase IOException
import java.util.ArrayList;                                                                                     // importar la clase ArrayList
import java.util.List;                                                                                          // importar la clase List

public class FormDialogUser extends JDialog {                                                                   // Declaración de la clase FormDialogUser

    private final JTextField[] fields;                                                                          // Declaración de un arreglo de JTextField
    private boolean confirmed;                                                                                  // Declaración de un booleano
    private final List<JCheckBox> taskCheckBoxes;                                                               // Declaración de una lista de JCheckBox

    public FormDialogUser(ProjectManager owner, String title, String[] fieldLabels) throws IOException {        // Constructor de la clase FormDialogUser
        super();                                                                                                // Llama al constructor de la clase padre
        setLayout(new BorderLayout());                                                                          // Establece el layout del formulario

        JsonFileManager jsonFileManager = new JsonFileManager();                                                // Crea un nuevo JsonFileManager
        List<Task> tasks = new ArrayList<>();                                                                   // Crea una nueva lista de tareas
        jsonFileManager.loadTasks(tasks);                                                                       // Carga las tareas en la lista
                                                                                                                // Panel para los campos de entrada
        JPanel formPanel = new JPanel();                                                                        // Crea un nuevo JPanel
        formPanel.setLayout(new GridBagLayout());                                                               // Cambiar a GridBagLayout
                                                                                                                // Poner un título al formulario
        setTitle(title);                                                                                        // Establece el título del formulario
        GridBagConstraints gbc = new GridBagConstraints();                                                      // Crea un nuevo GridBagConstraints
        gbc.fill = GridBagConstraints.HORIZONTAL;                                                               // Relleno horizontal
        gbc.insets = new Insets(5, 5, 5, 5);                                                                    // Espacios entre componentes
                                                                                                                // Crear campos de texto
        fields = new JTextField[fieldLabels.length];                                                            // Inicializa el arreglo de JTextField
        for (int i = 0; i < fieldLabels.length - 1; i++) {                                                      // Recorre los campos de texto
            gbc.gridx = 0;                                                                                      // Columna 0
            gbc.gridy = i;                                                                                      // Fila i
            gbc.weightx = 0.1;                                                                                  // Ancho del label
            formPanel.add(new JLabel(fieldLabels[i]), gbc);                                                     // Añade una etiqueta al panel
                                                                                                                // Crear un campo de texto
            fields[i] = new JTextField(20);                                                                     // Crea un campo de texto
            gbc.gridx = 1;                                                                                      // Columna 1
            gbc.weightx = 1.0;                                                                                  // Ancho del campo de texto
                                                                                                                // Cambiar el tamaño de los campos de texto
            if (i < 3) {                                                                                        // Si es un campo de texto normal
                fields[i].setPreferredSize(new Dimension(200, 25));                                             // Cambia el tamaño aquí
            }
            formPanel.add(fields[i], gbc);                                                                      // Añade el campo de texto al panel
        }
                                                                                                                // Panel para la selección de tareas
        gbc.gridx = 0;                                                                                          // Columna 0
        gbc.gridy = fieldLabels.length;                                                                         // Fila fieldLabels.length
        gbc.gridwidth = 2;                                                                                      // Ancho 2
        formPanel.add(new JLabel("Select Tasks"), gbc);                                                         // Añade una etiqueta al panel
                                                                                                                // Panel para las tareas
        JPanel taskPanel = new JPanel();                                                                        // Crea un nuevo JPanel
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));                                        // Cambiar a BoxLayout
                                                                                                                // Crear checkboxes para las tareas
        taskCheckBoxes = new ArrayList<>();                                                                     // Inicializa la lista de checkboxes
        for (Task task : tasks) {                                                                               // Recorre las tareas
            JCheckBox checkBox = new JCheckBox(task.getId() + " - " + task.getName());                     // Crea un checkbox
            taskCheckBoxes.add(checkBox);                                                                       // Añade el checkbox a la lista
            taskPanel.add(checkBox);                                                                            // Añade el checkbox al panel
        }
                                                                                                                // Añadir un JScrollPane para la lista de tareas
        JScrollPane taskScrollPane = new JScrollPane(taskPanel);                                                // Crea un JScrollPane
        taskScrollPane.setPreferredSize(new Dimension(300, 100));                                               // Cambia el tamaño del JScrollPane
                                                                                                                // Añadir el JScrollPane al formulario
        gbc.gridy = fieldLabels.length + 1;                                                                     // Fila fieldLabels.length + 1
        formPanel.add(taskScrollPane, gbc);                                                                     // Añade el JScrollPane al panel
                                                                                                                // Añadir el panel al centro del formulario
        add(formPanel, BorderLayout.CENTER);                                                                    // Añade el panel al centro del formulario
                                                                                                                // Panel para los botones
        JPanel buttonPanel = new JPanel();                                                                      // Crea un nuevo JPanel
        JButton okButton = new JButton("OK");                                                                   // Crea un botón de OK
        JButton cancelButton = new JButton("Cancel");                                                           // Crea un botón de Cancel
                                                                                                                // Acción del botón OK
        okButton.addActionListener(e -> {                                                                       // Añade un ActionListener al botón
            confirmed = true;                                                                                   // Confirma el formulario
            setVisible(false);                                                                                  // Oculta el formulario
            ProjectPanel projectPanel = new ProjectPanel();                                                     // Crea un nuevo ProjectPanel
            projectPanel.refreshProjects();                                                                     // Refresca los proyectos
        });
                                                                                                                // Acción del botón Cancel
        cancelButton.addActionListener(e -> {                                                                   // Añade un ActionListener al botón
            confirmed = false;                                                                                  // No confirma el formulario
            setVisible(false);                                                                                  // Oculta el formulario
        });
                                                                                                                // Añade los botones al panel
        buttonPanel.add(okButton);                                                                              // Añade el botón OK al panel
        buttonPanel.add(cancelButton);                                                                          // Añade el botón Cancel al panel
        add(buttonPanel, BorderLayout.SOUTH);                                                                   // Añade el panel al sur del formulario
                                                                                                                // Configuración básica del formulario
        pack();                                                                                                 // Empaqueta el formulario
        setLocationRelativeTo(owner);                                                                           // Establece la posición del formulario
    }
                                                                                                                // Método para obtener los valores ingresados, incluyendo la selección de tareas
    public String[] getFieldValues() {                                                                          // Método para obtener los valores ingresados
        String[] values = new String[fields.length + 1];                                                        // Crear un arreglo de String
        for (int i = 0; i < fields.length - 1; i++) {                                                           // Recorrer los campos de texto
            values[i] = fields[i].getText();                                                                    // Obtener el texto de cada campo
        }
                                                                                                                // Obtener las tareas seleccionadas
        List<String> selectedTasks = new ArrayList<>();                                                         // Crear una lista de tareas seleccionadas
        for (JCheckBox checkBox : taskCheckBoxes) {                                                             // Recorrer los checkboxes
            if (checkBox.isSelected()) {                                                                        // Si el checkbox está seleccionado
                                                                                                                // Separar el ID de la tarea seleccionada
                String taskId = checkBox.getText().split("-")[0].trim();                                        // Obtener el ID de la tarea
                System.out.println("Selected task: " + taskId);                                                 // Mostrar el ID de la tarea
                selectedTasks.add(taskId);                                                                      // Añadir el ID a la lista
            }
        }
                                                                                                                // Unir las tareas seleccionadas en una sola cadena separada por comas
        values[fields.length - 1] = String.join(",", selectedTasks);                                            // Unir los IDs de las tareas
                                                                                                                // Mostrar los valores ingresados
        System.out.println("Selected tasks: " + values[0]);                                                     // mostrar el nombre del usuario
        System.out.println("Selected tasks: " + values[1]);                                                     // mostrar el email del usuario
        System.out.println("Selected tasks: " + values[2]);                                                     // mostrar el teléfono del usuario
        System.out.println("Selected tasks: " + values[3]);                                                     // mostrar las tareas seleccionadas
        return values;
    }
                                                                                                                // Método para verificar si se confirmó el formulario
    public boolean isConfirmed() {                                                                              // Método para verificar si se confirmó el formulario
        return confirmed;                                                                                       // Retorna si se confirmó el formulario
    }
                                                                                                                // Método para prellenar campos (útil para actualizaciones)
    public void setFieldValue(int i, String value) {                                                            // Método para prellenar campos
        fields[i].setText(value);                                                                               // Establece el texto del campo
    }
                                                                                                                // Método para preseleccionar las tareas en los checkboxes
    public void setSelectedTasks(List<String> taskIds) {                                                        // Método para preseleccionar tareas
        for (JCheckBox checkBox : taskCheckBoxes) {                                                             // Recorrer los checkboxes
            String taskId = checkBox.getText().split("-")[0].trim();                                            // Obtener el ID de la tarea
            if (taskIds.contains(taskId)) {                                                                     // Si el ID está en la lista
                checkBox.setSelected(true);                                                                     // Seleccionar el checkbox
            }
        }
    }
}