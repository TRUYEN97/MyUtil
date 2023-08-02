/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.Keyword;
import com.tec02.common.RequestParam;
import com.tec02.common.RestAPI;
import com.tec02.common.RestUtil;
import com.tec02.common.Util;
import com.tec02.gui.model.PropertiesModel;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AppPackage {

    private final Repository appBackUp;
    private final String backupDir;
    private final StoreLoger loger;
    private final RestUtil restUtil;

    public AppPackage(RestAPI api, StoreLoger loger) throws IOException {
        this.restUtil = new RestUtil(api);
        this.backupDir = PropertiesModel.getConfig(Keyword.Store.LOCAL_BACKUP);
        this.appBackUp = new Repository(loger);
        this.loger = loger;
    }

    public void checkAppUpdate(RequestParam param) {
        removeAppFiles(this.appBackUp.setJsonArrayData(this.restUtil.getList(
                PropertiesModel.getConfig(Keyword.Url.Pc.GET_APP_INFO), param)));
        if (this.appBackUp.isEmpty()) {
            this.appBackUp.clear(new File(backupDir));
        }
        updateAppBackups();
    }

    public Map<Object, AppModel> getApps() {
        return this.appBackUp.getAppModels();
    }

    private void removeAppFiles(Map<Object, AppModel> appRemoves) {
        if (appRemoves == null) {
            return;
        }
        for (AppModel appModel : appRemoves.values()) {
            var files = appModel.getFiles();
            if (files == null || files.isEmpty()) {
                continue;
            }
            this.appBackUp.deleteApp(appModel.getFiles().values(), backupDir);
        }
    }

    private void updateAppBackups() {
        for (AppModel app : this.appBackUp.getAppModels().values()) {
            FileModel fileProgram = app.getFileProgram();
            checkUpdate(fileProgram, PropertiesModel.getConfig(Keyword.Url.FileProgram.GET_LAST_VERSION_DOWNLOAD));
            var files = app.getFiles();
            if (files == null || files.isEmpty()) {
                continue;
            }
            for (FileModel fileModel : files.values()) {
                checkUpdate(fileModel, PropertiesModel.getConfig(Keyword.Url.File.GET_LAST_VERSION_DOWNLOAD));
            }
        }
    }

    private void checkUpdate(FileModel fileProgram, String url) throws HeadlessException {
        File file;
        file = fileProgram.getLocalPath(backupDir).toFile();
        if (!file.exists()
                || !Util.md5File(file.getPath())
                        .equals(fileProgram.getMd5())) {
            if (this.restUtil.downloadFileSaveByPathOnServer(url,
                    RequestParam.builder().addParam("id", 
                            fileProgram.getId()), file.getPath())) {
                this.loger.addLog("Download", "%s -> ok", file.getPath());
            } else {
                this.loger.addLog("Download", "%s -> failed", file.getPath());
            }
        }
    }

}