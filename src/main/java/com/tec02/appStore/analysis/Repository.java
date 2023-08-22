/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.alibaba.fastjson.JSONArray;
import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.AppUpdateModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.MyObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Repository {

    private final Map<Object, AppUpdateModel> appModels;
    private final StoreLoger loger;

    public Repository(StoreLoger loger) {
        this.appModels = new HashMap<>();
        this.loger = loger;
    }

    public synchronized List<AppModel> setData(Collection<? extends AppModel> newAppModels) {
        if (newAppModels == null) {
            return null;
        }
        Map<Object, AppUpdateModel> temps = new HashMap<>();
        for (AppModel model : newAppModels) {
            AppUpdateModel newAppModel = new AppUpdateModel();
            newAppModel.setId(model.getId());
            newAppModel.setName(model.getName());
            newAppModel.setPassword(model.getPassword());
            newAppModel.setAlwaysRun(model.isAlwaysRun());
            newAppModel.setAlwaysUpdate(model.isAlwaysUpdate());
            newAppModel.setDescription(model.getDescription());
            newAppModel.setFileProgram(model.getFileProgram());
            newAppModel.setFiles(model.getFiles());
            temps.put(model.getId(), newAppModel);
        }
        Object id;
        AppUpdateModel newApp;
        List<AppModel> appRemoveds = new ArrayList<>();
        for (AppUpdateModel appModel : appModels.values()) {
            id = appModel.getId();
            if (temps.containsKey(id)) {
                newApp = temps.get(id);
                var oldList = appModel.getRemoveFiles();
                if (oldList != null && !oldList.isEmpty()) {
                    newApp.setRemoveFiles(oldList);
                }
                newApp.addRemoveFiles(
                        getAllFileNeedToRemove(newApp, appModel));
                newApp.addRemoveFile(
                        compareFile(newApp.getFileProgram(),
                                appModel.getFileProgram()
                        ));
            } else {
                appRemoveds.add(appModel);
            }
        }
        appModels.clear();
        appModels.putAll(temps);
        return appRemoveds;
    }

    public List<AppModel> setJsonArrayData(JSONArray object) {
        if (object == null) {
            return null;
        }
        return setData(MyObjectMapper.convertToList(object, AppModel.class));
    }

    public Map<Object, AppUpdateModel> getAppModels() {
        return appModels;
    }

    private FileModel compareFile(FileModel fileModelNew, FileModel fileModelOld) {
        if (fileModelNew == null
                || (fileModelOld != null && !fileModelNew.equals(fileModelOld))) {
            return fileModelOld;
        }
        return null;
    }

    private Map<Object, FileModel> getAllFileNeedToRemove(AppModel newApp, AppModel appModel) {
        var removes = compare(
                appModel.getFiles(),
                newApp.getFiles());
        return removes;
    }

    public boolean deleteAppFiles(AppUpdateModel app, String dir) {
        if (app == null) {
            return true;
        }
        boolean rs = true;
        var files = app.getRemoveFiles();
        if (files != null && !files.isEmpty()) {
            List<Object> idRemove = new ArrayList<>();
            for (FileModel fileModel : files.values()) {
                if (this.deleteFile(fileModel, dir)) {
                    idRemove.add(fileModel.getId());
                } else {
                    rs = false;
                }
            }
            for (Object id : idRemove) {
                files.remove(id);
            }
        }
        return rs;
    }

    public boolean deleteFile(FileModel fileModel, String dir) {
        if (fileModel == null) {
            return true;
        }
        File file;
        file = fileModel.getLocalPath(dir).toFile();
        boolean rs = true;
        if (file.exists()) {
            rs = file.delete();
            this.loger.addLog("REMOVE", "%s -> %s", file.getPath(), rs);
        }
        clearStore(file);
        return rs;
    }

    private void clearStore(File file) {
        File perent = file.getParentFile();
        if (perent != null) {
            String[] children = perent.list();
            if (children == null || children.length == 0) {
                perent.delete();
                clearStore(perent);
            }
        }
    }

    public void clear(File root) {
        if (!root.exists()) {
            return;
        }
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                clear(file);
            }
            file.delete();
        }
    }

    private Map<Object, FileModel> compare(Map<Object, FileModel> fileOlds, Map<Object, FileModel> fileNews) {
        Map<Object, FileModel> fileRemoveds = new HashMap<>();
        for (Object id : fileOlds.keySet()) {
            FileModel fileModelOld = fileOlds.get(id);
            if (!fileNews.containsKey(id) || !fileModelOld.equals(fileNews.get(id))) {
                fileRemoveds.put(id, fileModelOld);
            }
        }
        return fileRemoveds;
    }

    public boolean isEmpty() {
        return this.appModels.isEmpty();
    }

}
