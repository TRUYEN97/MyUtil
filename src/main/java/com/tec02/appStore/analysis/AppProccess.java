/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import com.tec02.appStore.AppConsole;
import com.tec02.appStore.model.AppModel;
import com.tec02.common.JOptionUtil;
import com.tec02.common.Keyword;
import com.tec02.communication.Communicate.Impl.Cmd.Cmd;
import com.tec02.common.PropertiesModel;
import java.io.File;
import java.nio.file.Path;
import javax.swing.Icon;
import javax.swing.JPasswordField;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Administrator
 */
public class AppProccess {

    private final String dir;
    private final Cmd cmd;
    private final AppConsole console;
    private final Timer timer;
    private Thread thread;
    private AppModel appModel;
    private Path runFile;
    private boolean needUpdate;

    public AppProccess() {
        this.dir = PropertiesModel.getConfig(Keyword.Store.LOCAL_APP);
        this.cmd = new Cmd();
        this.console = new AppConsole();
        this.timer = new Timer(10000, (e) -> {
            if (this.appModel.isAwaysRun()) {
                runApp();
            } else {
                AppProccess.this.timer.stop();
            }
        });
    }

    public AppModel getAppModel() {
        return appModel;
    }

    public Object getId() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getId();
    }

    public void setRunFile(AppModel appModel) {
        this.appModel = appModel;
        this.needUpdate = false;
        this.runFile = this.appModel.getFileProgram().getLocalPath(dir);
        if (this.appModel.isAwaysRun()) {
            this.timer.start();
        } else {
            this.timer.stop();
        }
    }

    public boolean isRuning() {
        return appModel != null && thread != null && thread.isAlive();
    }

    public void showConsole() {
        if (isRuning()) {
            this.console.display(String.format("Console: %s", appModel.getName()));
        }
    }

    public synchronized void runApp() {
        if (appModel == null || isRuning()) {
            return;
        }
        this.thread = new Thread(() -> {
            String password = getPassword();
            if (password != null && !password.isBlank()) {
                JPasswordField passwordField = new JPasswordField();
                JOptionUtil.showObject(passwordField, "Password");
                String pass = new String(passwordField.getPassword());
                if (!pass.equals(password)) {
                    return;
                }
            }
            String fileName = runFile.getFileName().toString().toLowerCase();
            String commandRun;
            if (fileName.endsWith(".jar")) {
                commandRun = String.format("cd %s && java -jar %s",
                        runFile.getParent(), fileName);
            } else {
                commandRun = String.format("cd %s && %s",
                        runFile.getParent(), fileName);
            }
            if (!cmd.insertCommand(commandRun)) {
                return;
            }
            this.console.clear();
            String line;
            while ((line = cmd.readLine()) != null) {
                this.console.append(String.format("%s\r\n", line));
            }
            this.console.dispose();
        });
        thread.start();
    }

    public String getDescription() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getDescription();
    }

    public String getAppName() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getName();
    }

    public Icon getIcon(int w, int h) {
        if (this.appModel == null) {
            return null;
        }
        return FileSystemView
                .getFileSystemView()
                .getSystemIcon(runFile.toFile(), w, h);
    }

    public String getPassword() {
        if (this.appModel == null) {
            return null;
        }
        return this.appModel.getPassword();
    }

    public File getFileLocation() {
        if (this.appModel == null) {
            return null;
        }
        return runFile.toFile();
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }
    
    public void setNeedToUpdate() {
        this.needUpdate = true;
    }

}
