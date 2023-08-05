/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.alibaba.fastjson.JSONArray;
import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.AppRemove;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.MyObjectMapper;
import com.tec02.common.Util;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Repository {

    private final Map<Object, AppModel> appModels;
    private final StoreLoger loger;

    public Repository(StoreLoger loger) {
        this.appModels = new HashMap<>();
        this.loger = loger;
    }

    public synchronized Map<Object, AppRemove> setData(Collection<AppModel> newAppModels) {
        Map<Object, AppRemove> appRemoveds = new HashMap<>();
        if (newAppModels == null) {
            return appRemoveds;
        }
        Map<Object, AppModel> temps = new HashMap<>();
        for (AppModel newAppModel : newAppModels) {
            temps.put(newAppModel.getId(), newAppModel);
        }
        Object id;
        AppRemove appRemove;
        for (AppModel appModel : appModels.values()) {
            id = appModel.getId();
            appRemove = new AppRemove();
            appRemove.setName(appModel.getName());
            if (temps.containsKey(id)) {
                appRemove.setDeleteAll(false);
                appRemove.setFileProgram(
                        compareFile(
                                temps.get(id).getFileProgram(),
                                appModel.getFileProgram()));
                appRemove.setFiles(getAllFileNeedToRemove(temps.get(id), appModel));
                if (appRemove.getFileProgram() != null || !appRemove.getFiles().isEmpty()) {
                    appRemoveds.put(id, appRemove);
                }
            } else {
                appRemove.setDeleteAll(true);
                appRemove.setFileProgram(appModel.getFileProgram());
                appRemove.setFiles(appModel.getFiles());
                appRemoveds.put(id, appRemove);
            }
        }
        appModels.clear();
        appModels.putAll(temps);
        return appRemoveds;
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

    public void deleteApp(AppRemove app, String dir) {
        if (app == null) {
            return;
        }
        if (app.isDeleteAll()) {
            Util.deleteFolder(app.getLocalPath(dir).toFile());
        } else {
            deleteFile(app.getFileProgram(), dir);
            for (FileModel fileModel : app.getFiles().values()) {
                deleteFile(fileModel, dir);
            }
        }
    }

    public void deleteFile(FileModel fileModel, String dir) {
        if (fileModel == null) {
            return;
        }
        File file;
        file = fileModel.getLocalPath(dir).toFile();
        if (file.exists()) {
            this.loger.addLog("REMOVE", "%s -> %s", file.getPath(), file.delete());
        }
        clearStore(file);
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

    public Map<Object, AppRemove> setJsonArrayData(JSONArray object) {
        return setData(MyObjectMapper.convertToList(object, AppModel.class));
    }

    public Map<Object, AppModel> getAppModels() {
        return appModels;
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
