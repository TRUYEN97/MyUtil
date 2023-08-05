/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.common.API.Response;
import com.tec02.common.API.RestAPI;
import com.tec02.gui.model.MyFilterBuilder;
import com.tec02.gui.Panelupdate;
import com.tec02.gui.model.FilterModel;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class MyFilter extends Panelupdate {
   
    protected FilterModel model;
    protected RestAPI restAPI;

    /**
     * Creates new form Tilter
     *
     * @param model
     */
    public MyFilter(FilterModel model) {
        this(model.getAPI());
        update(model);
    }
    
    public MyFilter(RestAPI restAPI) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        initComponents();
        this.restAPI = restAPI;
        this.model = new FilterModel(restAPI);
    }

    public void setRestAPI(RestAPI restAPI) {
        this.restAPI = restAPI;
    }

    protected void update(FilterModel model) {
        this.model = model;
        this.restAPI = model.getAPI();
        this.setLayout(new GridLayout(model.count(), 1, 5, 0));
        for (FilterUnit unit : this.model.getFilter()) {
            this.add(unit);
            unit.update();
        }
    }

    public void setModel(FilterModel model) {
        update(model);
    }

    public static MyFilterBuilder getBuilder(RestAPI api) {
        return new MyFilterBuilder(api);
    }

    @Override
    public void update() {
        super.update();
        if (this.model != null) {
            for (FilterUnit filterUnit : this.model.getFilter()) {
                filterUnit.update();
            }
        }
    }

    public FilterModel getModel() {
        return model;
    }

    public void getListFromServerWithFilter(String filterName, String url) throws HeadlessException {
        FilterUnit filterUnit = this.getFilterUnit(filterName);
        MyFilter.this.getListFromServerWithFilter(filterUnit, url);
    }

    public void getListFromServerWithFilter(FilterUnit filterUnit, String url) throws HeadlessException {
        if (restAPI == null) {
            return;
        }
        if (filterUnit == null) {
            return;
        }
        Response response = MyFilter.this.restAPI.sendGet(url);
        if (response == null || !response.getStatus()) {
            return;
        }
        filterUnit.clear();
        List<Map<String, Object>> datas = response.getDatas();
        if (datas.isEmpty()) {
            return;
        }
        List<String> items = new ArrayList<>();
        for (Map<String, Object> data : datas) {
            items.add(String.valueOf(data.get("name")));
        }
        filterUnit.addItems(items);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 186, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 166, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public FilterUnit getFilterUnit(String key) {
        return this.model.getFilterUnit(key);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
