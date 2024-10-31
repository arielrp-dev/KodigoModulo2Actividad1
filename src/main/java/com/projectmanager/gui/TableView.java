package com.projectmanager.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TableView<T> extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public TableView(String[] columnNames) {
        setLayout(new BorderLayout());

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(columnNames, 0);

        // Crear la tabla con el modelo
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Añadir la tabla al panel
        add(scrollPane, BorderLayout.CENTER);
    }

    // Método para cargar datos en la tabla
    public void loadData(List<T> data, DataMapper<T> mapper) {
        tableModel.setRowCount(0); // Limpiar la tabla
        for (T item : data) {
            tableModel.addRow(mapper.map(item));
        }
    }

    // Interfaz funcional para mapear los datos del objeto T a la tabla
    @FunctionalInterface
    public interface DataMapper<T> {
        Object[] map(T item);
    }

    // Método para limpiar la tabla
    public void clearTable() {
        tableModel.setRowCount(0);
    }
}