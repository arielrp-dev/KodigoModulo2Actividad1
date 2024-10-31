package com.projectmanager.gui;

import com.projectmanager.services.ProjectManager;                                      // Importa la clase ProjectManager

import javax.swing.*;                                                                   // Importa la clase javax.swing.JFrame
import java.awt.*;                                                                      // Importa la clase java.awt.BorderLayout

public class FormDialog extends JDialog {

    private final JTextField[] fields;                                                  // Declara un arreglo de JTextField
    JComboBox<String> statusComboBox;                                                   // Declara un JComboBox de String
    private boolean confirmed;                                                          // Declara un booleano

    public FormDialog(ProjectManager owner, String title, String[] fieldLabels) {       // Constructor de la clase FormDialog
        super();                                                                        // Llama al constructor de la clase padre
        setLayout(new BorderLayout());                                                  // Establece el layout del formulario
                                                                                        // Panel para los campos de entrada
        JPanel formPanel = new JPanel();                                                // Crea un nuevo JPanel
        formPanel.setLayout(new GridLayout(fieldLabels.length, 2));                // Establece el layout del panel
                                                                                        // Poner un título al formulario
        setTitle(title);                                                                // Establece el título del formulario
                                                                                        // Crear campos de texto
        fields = new JTextField[fieldLabels.length];                                    // Inicializa el arreglo de JTextField
        for (int i = 0; i < fieldLabels.length-1; i++) {                                // Recorre los campos de texto
            formPanel.add(new JLabel(fieldLabels[i]));                                  // Añade una etiqueta al panel
            fields[i] = new JTextField(20);                                     // Crea un campo de texto
            formPanel.add(fields[i]);                                                   // Añade el campo de texto al panel
        }
                                                                                        // Añade un ComboBox para el estado
        formPanel.add(new JLabel(fieldLabels[fieldLabels.length-1]));                   // Añade una etiqueta al panel
        String[] statusOptions = {"Pendiente", "En progreso", "Completado"};            // Opciones para el ComboBox
        statusComboBox = new JComboBox<>(statusOptions);                                // Crea un ComboBox con las opciones
        formPanel.add(statusComboBox);                                                  // Añade el ComboBox al panel
        add(formPanel, BorderLayout.CENTER);                                            // Añade el panel al centro del formulario
                                                                                        // Panel para los botones
        JPanel buttonPanel = new JPanel();                                              // Crea un nuevo JPanel
        JButton okButton = new JButton("OK");                                      // Crea un botón de OK
        JButton cancelButton = new JButton("Cancel");                              // Crea un botón de Cancel
                                                                                        // Acción del botón OK
        okButton.addActionListener(e -> {                                               // Añade un ActionListener al botón
            confirmed = true;                                                           // Confirma el formulario
            setVisible(false);                                                          // Oculta el formulario
            ProjectPanel projectPanel = new ProjectPanel();                             // Crea un nuevo ProjectPanel
            projectPanel.refreshProjects();                                             // Refresca los proyectos
        });
                                                                                        // Acción del botón Cancel
        cancelButton.addActionListener(e -> {                                           // Añade un ActionListener al botón
            confirmed = false;                                                          // No confirma el formulario
            setVisible(false);                                                          // Oculta el formulario
        });
                                                                                        // Añade los botones al panel
        buttonPanel.add(okButton);                                                      // Añade el botón OK al panel
        buttonPanel.add(cancelButton);                                                  // Añade el botón Cancel al panel
        add(buttonPanel, BorderLayout.SOUTH);                                           // Añade el panel al sur del formulario
                                                                                        // Configuración básica del formulario
        pack();                                                                         // Empaqueta el formulario
        setLocationRelativeTo(owner);                                                   // Establece la posición del formulario
    }
                                                                                        // Método para obtener los valores ingresados
    public String[] getFieldValues() {                                                  // Método para obtener los valores ingresados
        String[] values = new String[fields.length];                                    // Crea un arreglo de String
        for (int i = 0; i < fields.length - 1; i++) {                                   // Recorre los campos de texto
            values[i] = fields[i].getText();                                            // Obtiene el texto de cada campo
        }
        values[fields.length-1] = (String) statusComboBox.getSelectedItem();            // Obtiene el estado seleccionado
        return values;                                                                  // Retorna los valores ingresados
    }
                                                                                        // Método para verificar si se confirmó el formulario
    public boolean isConfirmed() {                                                      // Método para verificar si se confirmó el formulario
        return confirmed;                                                               // Retorna si se confirmó el formulario
    }
                                                                                        // Método para prellenar campos (útil para actualizaciones)
    public void setFieldValue(int i, String value) {                                    // Método para prellenar campos
        fields[i].setText(value);                                                       // Establece el texto del campo
    }
}