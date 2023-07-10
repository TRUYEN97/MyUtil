/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.tec02.gui.frameGui.Component.MyChooser;
import java.awt.HeadlessException;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class RestUtil {

    private final RestAPI aPI;

    public RestUtil(RestAPI aPI) {
        this.aPI = aPI;
    }

    public RestAPI getApi() {
        return aPI;
    }

    public boolean addNew(String url, RequestParam urlParam, JsonBodyAPI bodyAPI) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendPost(url, urlParam, bodyAPI);
        return !response.isFailStatusAndShowMessage();
    }

    public boolean uploadFile(String url, JsonBodyAPI bodyAPI, String filePath) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, String.format("file is not exists! path: %s", filePath));
            return false;
        }
        FileInfo fileInfo = new FileInfo(FileInfo.type.FILE);
        fileInfo.setFile(file);
        fileInfo.setName(file.getName());
        Response response = this.aPI.uploadFile(url, bodyAPI,fileInfo);
        return !response.isFailStatusAndShowMessage();
    }
    
    public boolean downloadFileSaveByChooseFile(String url, RequestParam param) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.downloadFile(url, param, (jsono) -> {
            String name = jsono.getString("name");
            MyChooser chooser = new MyChooser();
            if(chooser.showSaveDialog(null, name) == JFileChooser.APPROVE_OPTION){
                return chooser.getSelectedFile();
            }
            return null;
        });
        return !response.isFailStatusAndShowMessage();
    }

    public <T> List<T> getList(String url, RequestParam param) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return null;
        }
        Response response = this.aPI.sendGet(url, param);
        if (response.isFailStatusAndShowMessage()) {
            return null;
        }
        return response.getDatas();
    }

    public boolean delete(String url, RequestParam param) {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendDelete(url, param);
        return !response.isFailStatusAndShowMessage();
    }
    
    public boolean update(String url, RequestParam queryParam, JsonBodyAPI bodyAPI){
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendPut(url, queryParam, bodyAPI);
        return !response.isFailStatusAndShowMessage();
    }
}
