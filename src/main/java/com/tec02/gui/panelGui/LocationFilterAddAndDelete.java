/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.alibaba.fastjson.JSONObject;
import com.tec02.common.JsonBodyAPI;
import com.tec02.common.QueryParam;
import com.tec02.common.Response;
import com.tec02.common.RestAPI;
import com.tec02.gui.frameGui.Component.MyTable;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class LocationFilterAddAndDelete extends LocationFilter {

    private String getUrl;
    private String addUrl;
    private MyTable myTable;
    private String deleteUrl;

    public LocationFilterAddAndDelete(RestAPI api, String urlProduct, String urlStation, String urlLine) {
        super(api, urlProduct, urlStation, urlLine);
    }

    public void setMyTable(MyTable myTable) {
        this.myTable = myTable;
    }

    public MyTable getMyTable() {
        return myTable;
    }

    public void setUrlGet(String getUrl) {
        this.getUrl = getUrl;
    }

    public String getGetUrl() {
        return getUrl;
    }

    public void setUrlAdd(String addUrl) {
        this.addUrl = addUrl.trim();
    }

    public String getAddUrl() {
        return addUrl;
    }

    public void getList() throws HeadlessException {
        if(getUrl == null){
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
        Response response = this.restAPI.sendGet(getUrl,
                QueryParam.builder().addParam("pName", pSelected)
                        .addParam("sName", sSelected).addParam("lName", lSelected));
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

    public void addNew(String name) throws HeadlessException {
        // TODO add your handling code here:
        if(addUrl == null){
            JOptionPane.showMessageDialog(null, "url == null");
            return;
        }
        String pSelected = getProduct();
        String sSelected = getStation();
        String lSelected = getLine();
        if (pSelected == null || sSelected == null || lSelected == null) {
            JOptionPane.showMessageDialog(null, "Location invalid!");
            return;
        }
        if (name.isBlank() || !name.matches("^[0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Pc name invalid!");
            return;
        }
        Response response = this.restAPI.sendPost(addUrl,
                QueryParam.builder().addParam("pName", pSelected)
                        .addParam("sName", sSelected)
                        .addParam("lName", lSelected),
                JsonBodyAPI.builder().put("name", String.format("%s-%s", sSelected, name)));
        if (response.isFailStatusAndShowMessage()) {
            return;
        }
        this.getList();
    }

    public void deleteSeleled() {
        if(deleteUrl == null){
            JOptionPane.showMessageDialog(null, "url == null");
            return;
        }
        if(myTable == null){
            JOptionPane.showMessageDialog(null, "table == null");
            return;
        }
        int option = JOptionPane.showConfirmDialog(null, "Do you want to delete this List?", "Warning", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }
        int[] indexs = myTable.getSelectedRows();
        Response response = restAPI.sendDelete(deleteUrl,
                QueryParam.builder()
                        .addParam("id",
                                myTable.getListValue(indexs, "id")));
        if (!response.getStatus()) {
            JOptionPane.showMessageDialog(null, response.getMessage());
        }
        getList();
    }

    public void setUrlDelete(String url) {
        this.deleteUrl = url;
    }
    
}
