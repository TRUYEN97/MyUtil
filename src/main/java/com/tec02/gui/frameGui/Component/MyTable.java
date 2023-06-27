/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui.Component;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class MyTable {

    private List<String> columns;
    private List<Object[]> rows;
    private final JTable table;
    private DefaultTableModel model;

    public MyTable(JTable table) {
        if (table == null) {
            throw new NullPointerException("Table cannot be null");
        }
        this.table = table;
        rows = new ArrayList<>();
    }

    public void initTable(Collection<String> cols) {
        initTable(cols, null, null);
    }

    public void setMouseAdapter(MouseAdapter mouseAdapter) {
        if (this.table == null) {
            return;
        }
        this.table.addMouseListener(mouseAdapter);
    }

    public List<String> getColumns() {
        return columns;
    }

    public void initTable(Collection<String> cols, int[] sizes, boolean[] editables) {
        this.columns = new ArrayList<>(cols);
        if (cols == null || cols.isEmpty()) {
            return;
        }
        clear();
        if (sizes == null) {
            sizes = getDefaultSizes(cols.size());
        }
        this.model = new javax.swing.table.DefaultTableModel(null, cols.toArray()) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (editables == null) {
                    return false;
                }
                return editables[columnIndex];
            }
        };
        this.table.setModel(this.model);
        this.table.getTableHeader().setReorderingAllowed(true);//
        this.table.setShowGrid(true);
        float onePercent = this.table.getWidth() / 100;
        int sizeLenth = sizes.length;
        for (int i = 0; i < cols.size(); i++) {
            int w = (int) (sizeLenth <= i ? (onePercent * sizes[sizeLenth - 1]) : (onePercent * sizes[i]));
            setPropertiesColumn(i, w, JLabel.CENTER, JLabel.CENTER);
        }
    }

    public int getSelectedRow() {
        if (isNull(model)) {
            return -1;
        }
        return this.table.getSelectedRow();
    }

    public int getColumnCount() {
        if (isNull(model)) {
            return 0;
        }
        return this.model.getColumnCount();
    }

    public int getSelectedRowCount() {
        if (isNull(table)) {
            return 0;
        }
        return this.table.getSelectedRowCount();
    }

    public int[] getSelectedRows() {
        if (isNull(model)) {
            return new int[]{};
        }
        return this.table.getSelectedRows();
    }

    public void clear() {
        if (isNull(model)) {
            return;
        }
        this.rows.clear();
        this.model.setRowCount(0);
    }

    public void deleteAll(int[] selectedRows) {
        if (selectedRows == null) {
            return;
        }
        for (int selectedRow : selectedRows) {
            this.delete(selectedRow);
        }
    }

    public void delete(int index) {
        if (isNull(model)) {
            return;
        }
        this.rows.remove(index);
        this.model.removeRow(index);
    }

    protected void addRow(Object[] row) {
        this.rows.add(row);
        if (isNull(model) || isNull(table)) {
            return;
        }
        if (row == null || row.length == 0) {
            return;
        }
        this.model.addRow(row);
        this.table.revalidate();
        this.table.repaint();
    }

    public void addRow(Map<String, Object> values) {
        List<Object> objects = new ArrayList<>();
        for (String column : columns) {
            if (values.containsKey(column)) {
                objects.add(values.get(column));
            } else {
                objects.add(null);
            }
        }
        this.addRow(objects.toArray());
    }

    public void setMapRows(List<Map<String, Object>> values) {
        if (values == null) {
            this.clear();
            return;
        }
        for (Map<String, Object> value : values) {
            addRow(value);
        }
    }

    public List<Map<String, Object>> getMapRowsWithIndexsColumns(int[] indexs, List<String> columns) {
        List<Map<String, Object>> maps = new ArrayList<>();
        if (indexs == null) {
            int rowCount = getRowCount();
            for (int index = 0; index < rowCount; index++) {
                maps.add(getDataWithCoumns(columns, index));
            }
        } else {
            for (int index : indexs) {
                maps.add(getDataWithCoumns(columns, index));
            }
        }
        return maps;
    }

    private Map<String, Object> getDataWithCoumns(List<String> columns1, int index) {
        Map<String, Object> data;
        data = new HashMap<>();
        for (String column : columns1) {
            data.put(column, getRowValue(index, column));
        }
        return data;
    }

    public List<Map<String, Object>> getMapRows() {
        return getMapRowsWithIndexsColumns(null, columns);
    }

    public int getRowCount() {
        if (isNull(model)) {
            return 0;
        }
        return this.model.getRowCount();
    }

    private void setPropertiesColumn(int index, int width, int alignment, int header) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alignment);
        this.table.getColumnModel().getColumn(index).setMinWidth(width);
        this.table.getColumnModel().getColumn(index).setCellRenderer(renderer);
        this.table.getColumnModel().getColumn(index).setResizable(true);
        DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer();
        renderer1.setHorizontalTextPosition(header);
        this.table.getColumnModel().getColumn(index).setHeaderRenderer(renderer);
    }

    private boolean isNull(Object object) {
        return object == null;
    }

    private int[] getDefaultSizes(int columnCount) {
        int[] s;
        s = new int[columnCount];
        for (int i = 0; i < columnCount; i++) {
            s[i] = 100 / columnCount;
        }
        return s;
    }

    public Object getRowValue(int row, String columnName) {
        int columnIndex;
        if ((columnIndex = columns.indexOf(columnName)) == -1) {
            return null;
        }
        return this.model.getValueAt(row, columnIndex);
    }

    public Object[] getRowValues(int[] rows, String columnName) {
        List values = getListValue(rows, columnName);
        return values.toArray();
    }

    public Object[] getRowValues(String columnName) {
        List values = getListValue(columnName);
        return values.toArray();
    }

    public List getListValue(int[] rows, String columnName) {
        List values = new ArrayList();
        if (rows == null) {
            int rowCount = getRowCount();
            for (int index = 0; index < rowCount; index++) {
                var val = getRowValue(index, columnName);
                if (isNull(val)) {
                    continue;
                }
                values.add(val);
            }
        } else {
            for (int index : rows) {
                var val = getRowValue(index, columnName);
                if (isNull(val)) {
                    continue;
                }
                values.add(val);
            }
        }
        return values;
    }

    public List getListValue(String columnName) {
        return getListValue(null, columnName);
    }

    public void setValueAt(int row, String columnName, Object value) {
        int columnIndex;
        if ((columnIndex = columns.indexOf(columnName)) == -1) {
            return;
        }
        this.model.setValueAt(value, row, columnIndex);
    }

    public Object getSelectedCell() {
        int row = getSelectedRow();
        int column = getSelectedColumn();
        if (row < 0 || column < 0) {
            return null;
        }
        return this.model.getValueAt(row, column);
    }

    private int getSelectedColumn() {
        if (isNull(model)) {
            return -1;
        }
        return this.table.getSelectedColumn();
    }

    public Map<String, Object> getMapSelectedCell() {
        int column = getSelectedColumn();
        if (column < 0) {
            return null;
        }
        Map<String, Object> values = new HashMap();
        values.put(COLUMN, this.model.getColumnName(column));
        values.put(VALUE, getSelectedCell());
        return values;
    }
    public static final String VALUE = "value";
    public static final String COLUMN = "column";
}
