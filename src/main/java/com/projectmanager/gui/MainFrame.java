package com.projectmanager.gui;

import javax.swing.*;                                                       // Importa la clase javax.swing.JFrame
import java.awt.*;                                                          // Importa la clase java.awt.BorderLayout
import java.io.IOException;                                                 // Importa la clase java.io.IOException

public class MainFrame extends JFrame {                                     // Clase MainFrame que extiende de JFrame

    private final JPanel mainPanel;                                         // Declara un JPanel
    private final ProjectPanel projectPanel;                                // Declara un ProjectPanel
    private final TaskPanel taskPanel;                                      // Declara un TaskPanel
    private final UserPanel userPanel;                                      // Declara un UserPanel

    public MainFrame() throws IOException {                                 // Constructor de la clase MainFrame
                                                                            // Configuración básica de la ventana principal
        setTitle("Project Manager");                                        // Establece el título de la ventana
        setSize(800, 600);                                                  // Establece el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                     // Establece la operación de cierre
        setLocationRelativeTo(null);                                        // Establece la posición de la ventana
                                                                            // Inicializar el panel principal
        mainPanel = new JPanel();                                           // Crea un nuevo JPanel
        mainPanel.setLayout(new BorderLayout());                            // Establece el layout del panel
                                                                            // Inicializar los paneles de proyecto, tarea y usuario
        projectPanel = new ProjectPanel();                                  // Crea un nuevo ProjectPanel
        taskPanel = new TaskPanel();                                        // Crea un nuevo TaskPanel
        userPanel = new UserPanel();                                        // Crea un nuevo UserPanel
                                                                            // Configurar la barra de menú
        setJMenuBar(new MenuBar(this));                                     // Establece la barra de menú
                                                                            // Añadir el panel principal a la ventana
        add(mainPanel);                                                     // Añade el panel principal a la ventana
                                                                            // Mostrar el panel de proyectos por defecto
        showProjectPanel();                                                 // Muestra el panel de proyectos
    }
                                                                            // Métodos para mostrar los paneles según la selección del usuario en el menú
    public void showProjectPanel() {                                        // Método para mostrar el panel de proyectos
        mainPanel.removeAll();                                              // Elimina todos los componentes del panel principal
        mainPanel.add(projectPanel, BorderLayout.CENTER);                   // Añade el panel de proyectos al centro del panel principal
        mainPanel.revalidate();                                             // Vuelve a validar el panel principal
        mainPanel.repaint();                                                // Vuelve a pintar el panel principal
    }
                                                                            // Método para mostrar el panel de tareas
    public void showTaskPanel() {                                           // Método para mostrar el panel de tareas
        mainPanel.removeAll();                                              // Elimina todos los componentes del panel principal
        mainPanel.add(taskPanel, BorderLayout.CENTER);                      // Añade el panel de tareas al centro del panel principal
        mainPanel.revalidate();                                             // Vuelve a validar el panel principal
        mainPanel.repaint();                                                // Vuelve a pintar el panel principal
    }
                                                                            // Método para mostrar el panel de usuarios
    public void showUserPanel() {                                           // Método para mostrar el panel de usuarios
        mainPanel.removeAll();                                              // Elimina todos los componentes del panel principal
        mainPanel.add(userPanel, BorderLayout.CENTER);                      // Añade el panel de usuarios al centro del panel principal
        mainPanel.revalidate();                                             // Vuelve a validar el panel principal
        mainPanel.repaint();                                                // Vuelve a pintar el panel principal
    }
                                                                            // Método para mostrar el panel de tareas por usuario
    public void showTaskByUserPanel() throws IOException {                  // Método para mostrar el panel de tareas por usuario
        TaskByUserPanel taskByUserPanel = new TaskByUserPanel();            // Crea un nuevo TaskByUserPanel
        mainPanel.removeAll();                                              // Elimina todos los componentes del panel principal
        mainPanel.add(taskByUserPanel, BorderLayout.CENTER);                // Añade el panel de tareas por usuario al centro del panel principal
        mainPanel.revalidate();                                             // Vuelve a validar el panel principal
        mainPanel.repaint();                                                // Vuelve a pintar el panel principal
    }
                                                                            // Método para mostrar el panel de tareas por proyecto
    public void showTaskByProjectPanel() throws IOException {               // Método para mostrar el panel de tareas por proyecto
        TaskByProjectPanel taskByProjectPanel = new TaskByProjectPanel();   // Crea un nuevo TaskByProjectPanel
        mainPanel.removeAll();                                              // Elimina todos los componentes del panel principal
        mainPanel.add(taskByProjectPanel, BorderLayout.CENTER);             // Añade el panel de tareas por proyecto al centro del panel principal
        mainPanel.revalidate();                                             // Vuelve a validar el panel principal
        mainPanel.repaint();                                                // Vuelve a pintar el panel principal
    }
                                                                            // Método principal para ejecutar la aplicación
    public static void main(String[] args) {                                // Método principal para ejecutar la aplicación
        SwingUtilities.invokeLater(() -> {                                  // Ejecuta la aplicación en un hilo de eventos
            MainFrame mainFrame;                                            // Declara una instancia de MainFrame
            try {                                                           // Manejo de excepciones
                mainFrame = new MainFrame();                                // Crea una nueva instancia de MainFrame
            } catch (IOException e) {                                       // Captura una excepción de tipo IOException
                throw new RuntimeException(e);                              // Lanza una excepción de tipo RuntimeException
            }
            mainFrame.setVisible(true);                                     // Hace visible la ventana principal
        });
    }
}