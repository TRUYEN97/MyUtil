/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.tec02.gui.frameGui;

import com.alibaba.fastjson.JSONObject;
import com.tec02.common.JsonBodyAPI;
import com.tec02.common.QueryParam;
import com.tec02.common.Response;
import com.tec02.common.RestAPI;
import com.tec02.gui.frameGui.Component.PopupMenu;
import com.tec02.gui.frameGui.Component.MyTable;
import com.tec02.gui.panelGui.LocationFilter;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class ManageEntityHaveLocation extends AbsDisplayAble {

    private final LocationFilter locationFilter;
    private final MyTable myTable;
    private final RestAPI aPI;
    private PopupMenu menu;
    private String getUrl;
    private String addUrl;
    private String deleteUrl;
    private static final ShowText showText;
    static {
        showText = new ShowText();
    }

    /**
     * Creates new form ManagePC
     *
     * @param aPI
     * @param productGet
     * @param stationGet
     * @param lineGet
     */
    public ManageEntityHaveLocation(RestAPI aPI, String productGet, String stationGet, String lineGet) {
        super();
        initComponents();
        addSubFrame(showText);
        this.aPI = aPI;
        this.myTable = new MyTable(tbShow);
        this.locationFilter = new LocationFilter(
                aPI, productGet, stationGet, lineGet);
        this.menu = new PopupMenu();
        this.myTable.setMouseAdapter(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu != null && e.getButton() == MouseEvent.BUTTON3) {
                    menu.show(tbShow, e.getX(), e.getY());
                }else if(e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1){
                    Map<String, Object> cellValue = myTable.getMapSelectedCell();
                    if(cellValue == null){
                        return;
                    }
                    String columnName = String.valueOf(cellValue.get(MyTable.COLUMN));
                    String value = String.valueOf(cellValue.get(MyTable.VALUE));
                    showText.display(columnName, value);
                }
            }
        });
        this.pnFilter.add(this.locationFilter);
        this.locationFilter.update();
    }

    public MyTable getTableModel() {
        return myTable;
    }

    public void setUrlAdd(String addUrl) {
        this.addUrl = addUrl;
    }

    public void setUrlGet(String getUrl) {
        this.getUrl = getUrl;
    }

    public void setUrlDelete(String urlDetlete) {
        this.deleteUrl = urlDetlete;
    }

    public void setMenu(PopupMenu menu) {
        this.menu = menu;
    }

    public PopupMenu getMenu() {
        return menu;
    }
    
     public void getList(String name) throws HeadlessException {
        if (getUrl == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return;
        }
        if (myTable == null) {
            return;
        }
        this.myTable.clear();
        String pSelected = getProduct();
        String sSelected = getStation();
        String lSelected = getLine();
        Response response = this.aPI.sendGet(getUrl,
                QueryParam.builder().addParam("pName", pSelected)
                        .addParam("sName", sSelected)
                        .addParam("lName", lSelected)
                        .addParam("name", name));
        if (response.isFailStatusAndShowMessage()) {
            return;
        }
        List<JSONObject> items = response.getDatas();
        if (items == null || items.isEmpty()) {
            return;
        }
        this.myTable.initTable(items.get(0).keySet());
        for (var item : items) {
            this.myTable.addRow(item);
        }
    }

    public void addNew(JsonBodyAPI bodyAPI) throws HeadlessException {
        // TODO add your handling code here:
        if (addUrl == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return;
        }
        String pSelected = getProduct();
        String sSelected = getStation();
        String lSelected = getLine();
        Response response = this.aPI.sendPost(addUrl,
                QueryParam.builder().addParam("pName", pSelected)
                        .addParam("sName", sSelected)
                        .addParam("lName", lSelected), bodyAPI);
        if (response.isFailStatusAndShowMessage()) {
            return;
        }
        this.getList(null);
    }

    public void deleteSeleled() {
        if (deleteUrl == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return;
        }
        if (myTable == null) {
            JOptionPane.showMessageDialog(null, "table == null");
            return;
        }
        int[] indexs = myTable.getSelectedRows();
        if(indexs == null || indexs.length == 0){
            JOptionPane.showMessageDialog(null, "Nothing to delete");
            return;
        }
        int option = JOptionPane.showConfirmDialog(null, "Do you want to delete this List?", "Warning", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        Response response = this.aPI.sendDelete(deleteUrl,
                QueryParam.builder()
                        .addParam("id",
                                myTable.getListValue(indexs, "id")));
        if (!response.getStatus()) {
            JOptionPane.showMessageDialog(null, response.getMessage());
        }
        getList(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbShow = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btFind = new javax.swing.JButton();
        pnFilter = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tbShow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbShow);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        btFind.setText("Find");
        btFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFindActionPerformed(evt);
            }
        });

        pnFilter.setBackground(new java.awt.Color(153, 153, 255));
        pnFilter.setLayout(new javax.swing.BoxLayout(pnFilter, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Search name");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 56, Short.MAX_VALUE)
                        .addComponent(btFind, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 57, Short.MAX_VALUE))
                    .addComponent(pnFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSearch)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btFind)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFindActionPerformed
        // TODO add your handling code here:
        String name = txtSearch.getText();
        this.getList(name.isBlank()? null: name);
    }//GEN-LAST:event_btFindActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
       showMenu(evt);
    }//GEN-LAST:event_formMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        showMenu(evt);
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void showMenu(MouseEvent evt) {
        // TODO add your handling code here:
        if (menu != null && (evt.getButton() == MouseEvent.BUTTON3 || evt.getClickCount() > 1)) {
            menu.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    @Override
    public void display(String titleName) {
        super.display(titleName);
        this.locationFilter.refresh();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFind;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JTable tbShow;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables

    private String getProduct() {
        return this.locationFilter.getProduct();
    }

    private String getStation() {
        return this.locationFilter.getStation();
    }

    private String getLine() {
        return this.locationFilter.getLine();
    }
}
