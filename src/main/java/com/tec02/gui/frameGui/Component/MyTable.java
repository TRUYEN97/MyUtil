/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui.Component;

import com.tec02.gui.IAction;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final JTable table;
    private DefaultTableModel model;
    private PopupMenu menu;
    private PopupMenu selectedMenu;
    private IAction<MouseEvent> doubleClickAction;

    public MyTable(JTable table) {
        if (table == null) {
            throw new NullPointerException("Table cannot be null");
        }
        this.menu = new PopupMenu();
        this.selectedMenu = new PopupMenu();
        this.table = table;
        this.table.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                int column = table.columnAtPoint(point);
                if (row >= 0 && column >= 0) {
                    Object value = table.getValueAt(row, column);
                    table.setToolTipText(String.valueOf(value));
                }
            }
        });
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickdAction(e);
            }
        });
        Component parerent = this.table.getParent();
        if (parerent != null) {
            parerent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (getSelectedRowCount() > 0 && !e.isControlDown()) {
                        clearSelection();
                    }
                    mouseClickdAction(e);
                }
            });
        }
    }

    public void setModeSelection(int selectionMode) {
        this.table.setSelectionMode(selectionMode);
    }

    private void mouseClickdAction(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            if (!e.isControlDown()) {
                clearSelection();
            }
            return;
        }
        if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1
                && getSelectedRowCount() == 1 && doubleClickAction != null) {
            doubleClickAction.action(e);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (table.getSelectedRowCount() > 0 && selectedMenu != null && !selectedMenu.isEmpty()) {
                selectedMenu.show(e);
            } else if (menu != null && !menu.isEmpty()) {
                menu.show(e);
            }
        }
    }

    public PopupMenu getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(PopupMenu selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public void setMenu(PopupMenu menu) {
        this.menu = menu;
    }

    public PopupMenu getMenu() {
        return menu;
    }

    public void initTable(Collection<String> cols) {
        initTable(cols, null, null);
    }

    public void setDoubleClickAction(IAction<MouseEvent> doubleClickAction) {
        this.doubleClickAction = doubleClickAction;
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
        this.model.removeRow(index);
    }

    protected void addRow(Object[] row) {
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
        if (columns == null) {
            return;
        }
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

    public Map<String, Object> getDataWithCoumns(List<String> columns, int index) {
        Map<String, Object> data;
        data = new HashMap<>();
        if (columns == null) {
            return data;
        }
        for (String column : columns) {
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

    public <T> T getRowSelectedValue(String columnName) {
        int columnIndex;
        int rowSelected = getSelectedRow();
        if (columns == null || (columnIndex = columns.indexOf(columnName)) < 0 || rowSelected < 0) {
            return null;
        }
        var val = this.model.getValueAt(rowSelected, columnIndex);
        return val == null ? null : (T) val;
    }

    public Object getRowValue(int row, String columnName) {
        int columnIndex;
        if (columns == null || (columnIndex = columns.indexOf(columnName)) < 0) {
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

    public <T> List<T> getRowSelectedValues(String columnName) {
        return this.getListValue(getSelectedRows(), columnName);
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
        if (columns == null) {
            return;
        }
        if ((columnIndex = columns.indexOf(columnName)) < 0) {
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

    public CellValue getMapSelectedCell() {
        int column = getSelectedColumn();
        if (column < 0) {
            return null;
        }
        return new CellValue(this.model.getColumnName(column), getSelectedCell());
    }

    public static final String VALUE = "value";
    public static final String COLUMN = "column";

    public void setDatas(List<? extends Map> items, String... columns) {
        setDatas(items, List.of(columns));
    }

    public void setDatas(List<? extends Map> items, Collection<String> columns) {
        clear();
        if (items == null) {
            return;
        }
        if (columns != null && !columns.isEmpty()) {
            initTable(columns);
        } else if (!items.isEmpty()) {
            initTable(items.get(0).keySet());
        }
        for (Map<String, Object> map : items) {
            addRow(map);
        }
    }

    public List<? extends Map> getRowSelectedMapValues() {
        int[] index = getSelectedRows();
        if (index.length == 0) {
            return List.of();
        }
        return getMapRowsWithIndexsColumns(index, columns);
    }

    public Map<String, Object> getRowSelectedMapValue() {
        int index = getSelectedRow();
        if (index == -1) {
            return Map.of();
        }
        return getDataWithCoumns(columns, index);
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public boolean isDataEmpty() {
        return table.getRowCount() == 0;
    }

    public class CellValue {

        private final String coloum;
        private final Object value;

        public CellValue(String coloum, Object value) {
            this.coloum = coloum;
            this.value = value;
        }

        public String getColoum() {
            return coloum;
        }

        public <T> T getValue() {
            return (T) value;
        }

    }
}
