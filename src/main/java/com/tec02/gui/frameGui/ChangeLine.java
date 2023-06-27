/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tec02.gui.frameGui;

import com.tec02.common.JsonBodyAPI;
import com.tec02.common.QueryParam;
import com.tec02.common.Response;
import com.tec02.common.RestAPI;
import com.tec02.event.PopupMenuFilterAction;
import com.tec02.gui.frameGui.Component.MyTable;
import com.tec02.gui.panelGui.MyFilter;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.PopupMenuEvent;

/**
 *
 * @author Administrator
 */
public class ChangeLine extends AbsDisplayAble {

    private MyTable tableModel;
    private MyTable rootTabel;
    private MyFilter myFilter;
    private RestAPI aPI;
    private String putPc;

    /**
     * Creates new form ChangeLine
     *
     * @param api
     * @param lineUrl
     * @param rootTabel
     */
    public ChangeLine(RestAPI api, String lineUrl, MyTable rootTabel) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangeLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        initComponents();
        this.aPI = api;
        this.rootTabel = rootTabel;
        this.tableModel = new MyTable(tbShow);
        List<String> columns = List.of("id", "name", "product", "station", "line");
        this.tableModel.initTable(columns);
        this.myFilter = MyFilter.getBuilder(api).addFilter("Line",
                (input) -> {
                    if (input == null) {
                        return;
                    }
                    int rowNum = this.tableModel.getRowCount();
                    for (int i = 0; i < rowNum; i++) {
                        tableModel.setValueAt(i, "line", input);
                    }
                }, new PopupMenuFilterAction() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                getMyFilter().getListFromServerWithFilter(getFilterUnit(), lineUrl);
            }
        }).build();
        this.myFilter.getListFromServerWithFilter("Line", lineUrl);
        this.pnFilter.add(this.myFilter);
        this.myFilter.update();
    }

    public void setPutUrl(String putPc) {
        this.putPc = putPc;
    }

    @Override
    public void display(String titleName) {
        List<Map<String, Object>> datas = rootTabel.getMapRowsWithIndexsColumns(
                rootTabel.getSelectedRows(), this.tableModel.getColumns());
        this.tableModel.clear();
        this.tableModel.setMapRows(datas);
        super.display(titleName);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbShow = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        pnFilter = new javax.swing.JPanel();
        btApply = new javax.swing.JButton();

        tbShow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbShow);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        pnFilter.setLayout(new javax.swing.BoxLayout(pnFilter, javax.swing.BoxLayout.LINE_AXIS));

        btApply.setText("Apply");
        btApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btApplyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 36, Short.MAX_VALUE)
                        .addComponent(btApply)
                        .addGap(0, 44, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btApply)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(175, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btApplyActionPerformed
        // TODO add your handling code here:
        if (putPc == null) {
            return;
        }
        QueryParam param = QueryParam.builder().addParam("lName",
                this.myFilter.getModel().getSelected("Line"));
        List<Long> ids = this.tableModel.getListValue("id");
        Response response = this.aPI.sendPut(putPc, param,
                JsonBodyAPI.builder().put("ids", ids));
        if (response == null) {
            return;
        }
        JOptionPane.showMessageDialog(null, response.getMessage());
    }//GEN-LAST:event_btApplyActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btApply;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JTable tbShow;
    // End of variables declaration//GEN-END:variables
}
