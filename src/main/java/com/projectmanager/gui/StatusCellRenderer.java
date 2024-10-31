package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Obtener el valor del estado de la fila (puedes cambiar la columna 3 por la columna que almacene el estado)
        String status = (String) table.getValueAt(row, 3);  // Suponemos que la columna 3 es la que contiene el estado

        // Cambiar el color de fondo según el estado
        if (status.equalsIgnoreCase("Pendiente")) {
            cell.setBackground(Color.RED);
        } else if (status.equalsIgnoreCase("En progreso")) {
            cell.setBackground(Color.YELLOW);
        } else if (status.equalsIgnoreCase("Completado")) {
            cell.setBackground(Color.GREEN);
        } else {
            cell.setBackground(Color.WHITE); // Color por defecto
        }

        // Si la fila está seleccionada, mantener el color de selección predeterminado
        if (isSelected) {
            cell.setBackground(table.getSelectionBackground());
        }

        return cell;
    }
}
