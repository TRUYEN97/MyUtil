/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common.API;

import com.alibaba.fastjson.JSONObject;
import com.tec02.common.FileInfo;
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
        return addNew(url, urlParam, bodyAPI, true);
    }

    public boolean addNew(String url, RequestParam urlParam, JsonBodyAPI bodyAPI, boolean showMess) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendPost(url, urlParam, bodyAPI);
        return !response.isFailStatusAndShowMessage(showMess);
    }

    public boolean uploadFile(String url, JsonBodyAPI bodyAPI, String filePath) {
        return uploadFile(url, null, bodyAPI, filePath, true);
    }

    public boolean uploadFile(String url, JsonBodyAPI bodyAPI, String filePath, boolean showMess) {
        return uploadFile(url, null, bodyAPI, filePath, showMess);
    }

    public boolean uploadFile(String url, RequestParam param, JsonBodyAPI bodyAPI, String filePath) {
        return uploadFile(url, param, bodyAPI, filePath, true);
    }
    
    public boolean uploadFile(String url, RequestParam param, JsonBodyAPI bodyAPI, String filePath, boolean showMess){
        Response response = uploadFileWithResponse(url, param, bodyAPI, filePath, showMess);
        return response.getStatus();
    }

    public synchronized Response uploadFileWithResponse(String url, RequestParam param, JsonBodyAPI bodyAPI, String filePath, boolean showMess) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, String.format("file is not exists! path: %s", filePath));
            return null;
        }
        FileInfo fileInfo = new FileInfo(FileInfo.type.FILE);
        fileInfo.setFile(file);
        fileInfo.setName(file.getName());
        Response response = this.aPI.uploadFile(url, param, bodyAPI, fileInfo);
        response.isFailStatusAndShowMessage(showMess);
        return response;
    }
    
    public boolean downloadFileSaveByPathOnServer(String url, RequestParam param, String path){
        return downloadFileSaveByPathOnServer(url, param, path, false);
    }

    public boolean downloadFileSaveByPathOnServer(String url, RequestParam param, String path, boolean showMess) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.downloadFile(url, param, (jsono) -> {
            return Path.of(path).toFile();
        });
        return !response.isFailStatusAndShowMessage(showMess);
    }
    
    public boolean downloadFileSaveByChooseFile(String url, RequestParam param){
        return downloadFileSaveByChooseFile(url, param, true);
    }

    public boolean downloadFileSaveByChooseFile(String url, RequestParam param, boolean showMess) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.downloadFile(url, param, (jsono) -> {
            String name = jsono.getString("filename");
            MyChooser chooser = new MyChooser();
            if (chooser.showSaveDialog(null, name) == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            }
            return null;
        });
        return !response.isFailStatusAndShowMessage(showMess);
    }
    
    public <T> T getList(String url, RequestParam param){
        return getList(url, param, false);
    }
    

    public <T> T getList(String url, RequestParam param, boolean showMess) throws HeadlessException {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return null;
        }
        Response response = this.aPI.sendGet(url, param);
        if (response.isFailStatusAndShowMessage(showMess)) {
            return null;
        }
        return response.getData();
    }

    public boolean delete(String url, RequestParam param) {
        return delete(url, param, true);
    }
    
    public boolean delete(String url, RequestParam param, boolean showMess) {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendDelete(url, param);
        return !response.isFailStatusAndShowMessage(showMess);
    }

    public boolean update(String url, RequestParam queryParam, JsonBodyAPI bodyAPI) {
        return update(url, queryParam, bodyAPI, true);
    }
    
    public boolean update(String url, RequestParam queryParam, JsonBodyAPI bodyAPI, boolean showMess) {
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return false;
        }
        Response response = this.aPI.sendPut(url, queryParam, bodyAPI);
        return !response.isFailStatusAndShowMessage(showMess);
    }
}
