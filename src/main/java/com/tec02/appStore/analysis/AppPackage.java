/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.StoreLoger;
import com.tec02.appStore.model.AppModel;
import com.tec02.appStore.model.AppUpdateModel;
import com.tec02.appStore.model.FileModel;
import com.tec02.common.Keyword;
import com.tec02.API.RequestParam;
import com.tec02.API.Response;
import com.tec02.API.RestAPI;
import com.tec02.common.RestUtil;
import com.tec02.common.Util;
import com.tec02.common.PropertiesModel;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

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
        Util.deleteFolder(new File(backupDir));
        this.appBackUp = new Repository(loger);
        this.loger = loger;
    }

    public void checkAppUpdate(RequestParam param) {
        String url = PropertiesModel.getConfig(Keyword.Url.Pc.GET_APP_INFO);
        if (url == null) {
            JOptionPane.showMessageDialog(null, "url == null");
            return;
        }
        Response response = this.restUtil.getApi().sendGet(url, param);
        if (response.isFailStatusAndShowMessage(false)) {
            if (response.getCode() != 200) {
                return;
            }
        }
        removeAppFiles(this.appBackUp.setJsonArrayData(response.getData()));
        if (this.appBackUp.isEmpty()) {
            this.appBackUp.clear(new File(backupDir));
        }
        updateAppBackups();
    }

    public Map<Object, AppUpdateModel> getApps() {
        return this.appBackUp.getAppModels();
    }

    private void removeAppFiles(List<AppModel> appRemoves) {
        if (appRemoves == null) {
            return;
        }
        for (AppModel appModel : appRemoves) {
            this.appBackUp.clear(appModel.getLocalPath(backupDir).toFile());
        }
    }

    private void updateAppBackups() {
        for (AppUpdateModel app : this.appBackUp.getAppModels().values()) {
            FileModel fileProgram = app.getFileProgram();
            checkUpdate(fileProgram, PropertiesModel.getConfig(Keyword.Url.FileProgram.GET_LAST_VERSION_DOWNLOAD));
            this.appBackUp.deleteAppFiles(app, backupDir);
            var files = app.getFiles();
            if (files != null && !files.isEmpty()) {
                for (FileModel fileModel : files.values()) {
                    checkUpdate(fileModel,
                            PropertiesModel.getConfig(Keyword.Url.File.GET_LAST_VERSION_DOWNLOAD));
                }
            }

        }
    }

    private void checkUpdate(FileModel fileModel, String url) throws HeadlessException {
        File file;
        file = fileModel.getLocalPath(backupDir).toFile();
        if (!file.exists()
                || !Util.md5File(file.getPath())
                        .equals(fileModel.getMd5())) {
            if (this.restUtil.downloadFileSaveByPathOnServer(url,
                    RequestParam.builder().addParam("id",
                            fileModel.getId()), file.getPath())) {
                this.loger.addLog("Download", "%s -> ok", file.getPath());
            } else {
                this.loger.addLog("Download", "%s -> failed", file.getPath());
            }
        }
    }

}
