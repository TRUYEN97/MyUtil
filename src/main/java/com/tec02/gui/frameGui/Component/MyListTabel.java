/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui.Component;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Administrator
 * @param <T>
 */
public class MyListTabel<T> {

    private final JList<T> jList;
    private DefaultListModel<T> defaultListModel;

    public MyListTabel(JList<T> jList) {
        this.jList = jList;
        this.defaultListModel = new DefaultListModel<>();
        this.jList.setModel(defaultListModel);
    }


    public void addItem(T element) {
        if (element == null) {
            return;
        }
        this.defaultListModel.addElement(element);
    }

    public void reset() {
        this.defaultListModel.removeAllElements();
    }


    public List<T> getAllItem() {
        List<T> rs = new ArrayList<>();
        Enumeration<T> enumeration = this.defaultListModel.elements();
        while (enumeration.hasMoreElements()) {
            T nextElement = enumeration.nextElement();
            if (nextElement == null) {
                continue;
            }
            rs.add(nextElement);
        }
        return rs;
    }


    public boolean contains(T element) {
        if (element == null) {
            return false;
        }
        Enumeration<T> enumeration = this.defaultListModel.elements();
        while (enumeration.hasMoreElements()) {
            T nextElement = enumeration.nextElement();
            if (nextElement == null) {
                continue;
            }
            if (nextElement.equals(element)) {
                return true;
            }
        }
        return false;
    }


    public void addAllItem(List<T> rows) {
        if (rows == null || rows.isEmpty()) {
            return;
        }
        this.defaultListModel.addAll(rows);
    }

    public T getSelectioned() {
        return this.jList.getSelectedValue();
    }


    public List<T> getSelectedValuesList() {
        return this.jList.getSelectedValuesList();
    }

    public List<T> removeItemSelecteds() {
        List<T> allItem = new ArrayList<>(getSelectedValuesList());
        for (T t : allItem) {
            this.defaultListModel.removeElement(t);
        }
        return allItem;
    }


    public List<T> removeAllItem() {
        List<T> allItem = getAllItem();
        reset();
        return allItem;
    }


    public int getSelectedCount() {
        return getSelectedValuesList().size();
    }
   
    public int size() {
        return this.defaultListModel.getSize();
    }

 
    public void remove(int index) {
        this.jList.remove(index);
    }


    public void removeFirst() {
        if (size() <= 0) {
            return;
        }
        remove(0);
    }


    public void removeLast() {
        int size = size();
        if (size <= 0) {
            return;
        }
        remove(size - 1);
    }


    public void addModel(DefaultListModel<T> model) {
        this.defaultListModel = model;
        this.jList.setModel(model);
    }
}
