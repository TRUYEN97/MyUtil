/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.model.AppUpdateModel;
import com.tec02.common.Keyword;
import com.tec02.common.PropertiesModel;
import com.tec02.communication.socket.Unicast.Server.HandleManagement;
import com.tec02.communication.socket.Unicast.Server.Server;
import com.tec02.gui.frameGui.AbsDisplayAble;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class AppProccessManagemant {

    private final Map<Object, AppProcess> appProccesses;
    private boolean hasChange;
    private final Server server;
    private final HandleManagement handleManagement;

    public AppProccessManagemant(AbsDisplayAble view) throws Exception {
        this.appProccesses = new HashMap<>();
        this.server = new Server(PropertiesModel.getInteger(Keyword.Socket.PORT, 5000),
                (handler) -> {
                    handler.send("name");
                    try {
                        String str = handler.readLine();
                        if (str == null) {
                            return null;
                        }
                        str = str.trim();
                        if (str.equalsIgnoreCase("show")) {
                            view.display(null);
                        } else if (str.matches("^name:[a-z_A-Z|0-9|\\_|\\-]+$")) {
                            String name = str.trim().split(":")[1];
                            handler.send("welcome ".concat(name));
                            return name;
                        }
                        return null;
                    } catch (IOException ex) {
                        return null;
                    }
                },
                (handler, data) -> {
                    if (data == null) {
                        return;
                    }
                    data = data.trim();
                    if (data.equalsIgnoreCase("quit")) {
                        handler.disconnect();
                    } else if (data.equalsIgnoreCase("show")) {
                        view.display(null);
                    }
                });
        this.server.setDebug(PropertiesModel.getBoolean(Keyword.Socket.DEBUG, false));
        this.handleManagement = this.server.getHandlerManager();
        this.server.start();
    }

    public boolean isHasChange() {
        return hasChange;
    }

    public boolean containsKey(Object id) {
        return appProccesses.containsKey(id);
    }

    public void remove(Object id) {
        this.hasChange = true;
        AppProcess ap = appProccesses.remove(id);
        if (ap != null) {
            ap.clear();
        }
    }

    public AppProcess create(AppUpdateModel appModel) {
        if (appModel == null) {
            return null;
        }
        AppProcess ap;
        Object id = appModel.getId();
        if (appProccesses.containsKey(id)) {
            ap = appProccesses.get(id);
        } else {
            hasChange = true;
            ap = new AppProcess(handleManagement);
            appProccesses.put(id, ap);
        }
        ap.setRunFile(appModel);
        return ap;
    }

    public AppProcess get(Object id) {
        if (this.appProccesses.containsKey(id)) {
            return this.appProccesses.get(id);
        }
        return null;
    }

    public Collection<AppProcess> getAppProccesses() {
        hasChange = false;
        return appProccesses.values();
    }

}
