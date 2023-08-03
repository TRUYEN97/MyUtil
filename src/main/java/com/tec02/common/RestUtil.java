/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONObject;
import com.tec02.gui.frameGui.Component.MyChooser;
import java.awt.HeadlessException;
import java.io.File;
import java.nio.file.Path;
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
        response.setTextComponent(null);
        return !response.isFailStatusAndShowMessage();
    }
    
    public boolean uploadFile(String url, JsonBodyAPI bodyAPI, String filePath){
        return uploadFile(url, null, bodyAPI, filePath);
    }

    public boolean uploadFile(String url, RequestParam param, JsonBodyAPI bodyAPI, String filePath) throws HeadlessException {
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
        Response response = this.aPI.uploadFile(url, param, bodyAPI,fileInfo);
        response.setTextComponent(null);
        return !response.isFailStatusAndShowMessage();
    }
    
    public boolean downloadFileSaveByPathOnServer(String url, RequestParam param, String path) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.downloadFile(url, param, (jsono) -> {
            return Path.of(path).toFile();
        });
        return !response.isFailStatusAndShowMessage();
    }
    
    public boolean downloadFileSaveByChooseFile(String url, RequestParam param) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.downloadFile(url, param, (jsono) -> {
            String name = jsono.getString("filename");
            MyChooser chooser = new MyChooser();
            if(chooser.showSaveDialog(null, name) == JFileChooser.APPROVE_OPTION){
                return chooser.getSelectedFile();
            }
            return null;
        });
        response.setTextComponent(null);
        return !response.isFailStatusAndShowMessage();
    }

    public <T> T getList(String url, RequestParam param) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return null;
        }
        Response response = this.aPI.sendGet(url, param);
        response.setTextComponent(null);
        if (response.isFailStatusAndShowMessage()) {
            return null;
        }
        return response.getData();
    }

    public boolean delete(String url, RequestParam param) {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendDelete(url, param);
        response.setTextComponent(null);
        return !response.isFailStatusAndShowMessage();
    }
    
    public boolean update(String url, RequestParam queryParam, JsonBodyAPI bodyAPI){
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendPut(url, queryParam, bodyAPI);
        response.setTextComponent(null);
        return !response.isFailStatusAndShowMessage();
    }
}
