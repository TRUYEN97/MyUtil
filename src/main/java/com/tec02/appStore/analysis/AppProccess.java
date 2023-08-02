/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.appStore.analysis;

import Time.WaitTime.Class.TimeMs;
import com.tec02.appStore.model.AppModel;
import com.tec02.common.Keyword;
import com.tec02.communication.Communicate.Impl.Cmd.Cmd;
import com.tec02.gui.model.PropertiesModel;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Administrator
 */
public class AppProccess {

    private final Cmd cmd;
    private AppModel appModel;
    private Path runFile;

    public AppProccess() {
        this.cmd = new Cmd();
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
        this.runFile = this.appModel.getFileProgram()
                .getLocalPath(
                        PropertiesModel
                                .getConfig(Keyword.Store.LOCAL_APP));
    }

    public boolean runApp() {
        if (this.appModel == null) {
            return false;
        }
        if (!isRuning()) {
            run();
        }
        return true;
    }

    public boolean isRuning() {
        if (this.appModel == null) {
            return false;
        }
        try ( FileChannel fileChannel = FileChannel.open(runFile,StandardOpenOption.WRITE); 
                FileLock fileLock = fileChannel.tryLock()) {
            if (fileLock != null) {
                fileLock.release();
                return false;
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean run() {
        if (this.appModel == null) {
            return false;
        }
        String fileName = runFile.getFileName().toString().toLowerCase();
        String commandRun;
        if (fileName.endsWith(".jar")) {
            commandRun = String.format("cd %s && start %s",
                    runFile.getParent(), fileName);
        } else {
            commandRun = String.format("cd %s && start %s",
                    runFile.getParent(), fileName);
        }
        if (!cmd.sendCommand(commandRun)) {
            return false;
        }
        cmd.readAll(new TimeMs(500));
        return true;
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

}
