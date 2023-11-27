/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.tec02.Jmodel.Component.MyTable;
import com.tec02.Jmodel.IAction;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Administrator
 */
public class JOptionUtil {

    public static void newWithNameAndDescription(String title, INewAction approveAction, INewAction cancelAction) {
        JTextField txtName = new JTextField();
        JTextArea txtAreaDescripton = new JTextArea();
        txtAreaDescripton.setRows(5);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(txtAreaDescripton);
        Object[] components = {"Name", txtName, "Description", scrollPane};
        int result = JOptionPane.showOptionDialog(null, components, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        String name = txtName.getText();
        String des = txtAreaDescripton.getText();
        if (result == JOptionPane.OK_OPTION) {
            if (approveAction != null) {
                if (des == null || des.isBlank()) {
                    JOptionPane.showMessageDialog(null, "Description == null!");
                    return;
                }
                approveAction.action(name, des);
            }
        } else {
            if (cancelAction != null) {
                cancelAction.action(name, des);
            }
        }
    }

    public static <T> void getSelectionItem(String title, Collection<T> items,
            IAction<T> approveAction, IAction<T> cancelAction) {
        JComboBox<T> comboBox = new JComboBox<>();
        for (T lineItem : items) {
            comboBox.addItem(lineItem);
        }
        Object[] components = {"Select an Item:", comboBox};
        int result = JOptionPane.showOptionDialog(null, components, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        T itemSelected = (T) comboBox.getSelectedItem();
        if (result == JOptionPane.OK_OPTION) {
            if (approveAction != null) {
                approveAction.action(itemSelected);
            }
        } else {
            if (cancelAction != null) {
                cancelAction.action(itemSelected);
            }
        }
    }

    public static List<? extends Map> getTableSelectedItems(String title, List<? extends Map> items, String... columns) {
        JTable tbList = new JTable();
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tbList);
        MyTable myTable = new MyTable(tbList);
        if (items == null) {
            return null;
        }
        myTable.setDoubleClickAction((input) -> {
            var cell = myTable.getMapSelectedCell();
            JOptionPane.showMessageDialog(null, cell.getValue(), cell.getColoum(), JOptionPane.DEFAULT_OPTION);
        });
        myTable.setDatas(items, columns);
        Object[] components = {"Select an Item:", scrollPane};
        int result = JOptionPane.showOptionDialog(null, components, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        if (result == JOptionPane.OK_OPTION) {
            return myTable.getRowSelectedMapValues();
        } else {
            return null;
        }
    }

    public static <T extends Map> T getTableSelectedItem(String title, List<? extends Map> items, String... columns) {
        if (items == null) {
            return null;
        }
        JTable tbList = new JTable();
        MyTable myTable = new MyTable(tbList);
        myTable.setModeSelection(ListSelectionModel.SINGLE_SELECTION);
        myTable.setDoubleClickAction((input) -> {
            var cell = myTable.getMapSelectedCell();
            JOptionPane.showMessageDialog(null, cell.getValue(), cell.getColoum(), JOptionPane.DEFAULT_OPTION);
        });
        myTable.setDatas(items, columns);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tbList);
        Object[] components = {"Select an Item:", scrollPane};
        int result = JOptionPane.showOptionDialog(null, components, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        if (result == JOptionPane.OK_OPTION) {
            return (T) myTable.getRowSelectedMapValue();
        } else {
            return null;
        }
    }

    public static void login(String title, ILoginAction approveAction, ILoginAction cancelAction) {
        JTextField txtId = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        Object[] components = {"ID", txtId, "Password", txtPassword};
        int result = JOptionPane.showOptionDialog(null, components, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                null, null);
        String id = txtId.getText();
        char[] pass = txtPassword.getPassword();
        if (result == JOptionPane.OK_OPTION) {
            if (approveAction != null) {
                approveAction.action(id, pass);
            }
        } else {
            if (cancelAction != null) {
                cancelAction.action(id, pass);
            }
        }
    }

    public static void showObject(Object object, String name) {
        JOptionPane.showMessageDialog(null, object, name, JOptionPane.PLAIN_MESSAGE);
    }

    public static void showTextArea(String text, String name) {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Segoe UI", 0, 14));
        textArea.setText(text);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(textArea);
        showObject(scrollPane, name);
    }

    public static void showMessage(String format, Object... params) {
        JOptionPane.showMessageDialog(null, String.format(format, params));
    }

    public interface INewAction {

        void action(String name, String description);
    }

    public interface ILoginAction {

        void action(String name, char[] password);
    }
}
