/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui.Component;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author 21AK22
 */
public class MyChooser {

    private File[] files;
    private File file;
    private File currDir;
    private File newFile;

    public MyChooser() {
        this.currDir = FileSystemView.getFileSystemView().getHomeDirectory();
    }

    public MyChooser(String currFolder) {
        if (currFolder == null) {
            this.currDir = new File("..");
        } else {
            this.currDir = new File(currFolder);
        }
    }

    public File getCurrDir() {
        return currDir;
    }

    public void setCurrDir(File currDir) {
        this.currDir = currDir;
    }

    public int showSelectFolder(Component component) {
        if (component == null) {
            return - 1;
        }
        JFileChooser myChooser = shooseFolder();
        return saveCurrDir(myChooser, myChooser.showOpenDialog(component));
    }

    public int showSelectMutiFolder(Component component) {
        JFileChooser myChooser = showMutiFolder();
        return saveCurrDir(myChooser, myChooser.showOpenDialog(component));
    }

    public File getSelectedFile() {
        return this.file;
    }

    public File[] getSelectedFiles() {
        return this.files;
    }

    public int showSaveDialog(Component component, String defaultName) {
        if(defaultName == null){
            defaultName = String.format("%s.txt", System.currentTimeMillis());
        }
        JFileChooser myChooser = chooserFileOnly();
        myChooser.setDialogTitle("Save");
        if (defaultName != null) {
            myChooser.setSelectedFile(new File(defaultName));
        }
        return saveCurrDir(myChooser, myChooser.showSaveDialog(component));
    }

    public int showOpenFile(Component component, String dir) {
        JFileChooser myChooser = chooserFileOnly();
        return showOpen(myChooser, dir, component);
    }
    
    public int showOpenMitiFile(Component component, String dir) {
        JFileChooser myChooser = chooserFileOnly();
        myChooser.setMultiSelectionEnabled(true);
        return showOpen(myChooser, dir, component);
    }
    
    public int showOpenFileOrFolder(Component component, String dir) {
        JFileChooser myChooser = chooserFileOrFolder();
        return showOpen(myChooser, dir, component);
    }
    
    public int showOpenMutiFileOrFolder(Component component, String dir) {
        JFileChooser myChooser = chooserMutiFileOrFolder();
        return showOpen(myChooser, dir, component);
    }
    
    public File getNewFile() {
        return newFile;
    }

    public void setNewFile(File newFile) {
        this.newFile = newFile;
    }

    private int showOpen(JFileChooser myChooser, String dir, Component component) throws HeadlessException {
        myChooser.setDialogTitle("Open");
        if (dir != null) {
            myChooser.setSelectedFile(new File(dir));
        }
        return saveCurrDir(myChooser, myChooser.showOpenDialog(component));
    }

    private JFileChooser chooserMutiFileOrFolder() {
        JFileChooser myChooser = chooserFileOrFolder();
        myChooser.setMultiSelectionEnabled(true);
        return myChooser;
    }
    private JFileChooser chooserFileOrFolder() {
        JFileChooser myChooser = new JFileChooser(currDir);
        myChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        return myChooser;
    }
    
    private JFileChooser chooserFileOnly() {
        JFileChooser myChooser = new JFileChooser(currDir);
        myChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        myChooser.setFileFilter(new FileNameExtensionFilter("Csv", "csv"));
        myChooser.setFileFilter(new FileNameExtensionFilter("xlsx", "xlsx"));
        myChooser.setFileFilter(new FileNameExtensionFilter("Java", "jar"));
        myChooser.setFileFilter(new FileNameExtensionFilter("Text", "txt"));
        myChooser.setFileFilter(null);
        return myChooser;
    }

    private int saveCurrDir(JFileChooser myChooser, int result) {
        if (result == JFileChooser.APPROVE_OPTION) {
            this.currDir = myChooser.getSelectedFile();
            this.files = myChooser.getSelectedFiles();
            this.file = myChooser.getSelectedFile();
        }
        return result;
    }
    
    private JFileChooser shooseFolder() {
        JFileChooser myChooser = new JFileChooser(currDir);
        myChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        myChooser.setAcceptAllFileFilterUsed(false);
        return myChooser;
    }

    private JFileChooser showMutiFolder() {
        JFileChooser myChooser = shooseFolder();
        myChooser.setMultiSelectionEnabled(true);
        return myChooser;
    }
}
