package com.projectmanager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UIUtils {

    // Método para crear un botón con un texto y un ActionListener
    public static JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    // Método para mostrar un mensaje de error
    public static void showErrorMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Método para mostrar un mensaje informativo
    public static void showInfoMessage(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para mostrar un diálogo de confirmación
    public static int showConfirmDialog(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirm", JOptionPane.YES_NO_OPTION);
    }

    // Método para establecer la apariencia del panel
    public static void setPanelLayout(JPanel panel) {
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
