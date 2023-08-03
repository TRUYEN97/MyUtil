package com.tec02.SystemTray;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class MySystemTray {

    private final Component component;
    private TrayIcon trayIcon;
    private final SystemTray systemTray;
    private final PopupMenu menu;

    public MySystemTray(Component ui) throws Exception {
         if (!SystemTray.isSupported()) {
            throw new Exception("SystemTray is not supported");
        }
        this.systemTray = SystemTray.getSystemTray();
        this.component = ui;
        this.menu = new PopupMenu("Menu");
        this.menu.addSeparator();
        this.menu.add(showMenuItem());
        this.menu.add(hideMenuItem());
        this.menu.addSeparator();
    } 

    public boolean initTrayIcon(URL imagePath, String title) {
        if (imagePath == null || title == null) {
            return false;
        }
        Image image = Toolkit.getDefaultToolkit().createImage(imagePath);
        this.trayIcon = new TrayIcon(image, title, menu);
        mouseEvent();
        return this.systemTray != null && this.trayIcon != null;
    }

    public void apply() throws AWTException {
        if (isExsits()) {
            return;
        }
        trayIcon.setImageAutoSize(true);
        this.systemTray.add(trayIcon);
    }
    
    public void reject(){
        if (isExsits()) {
            this.systemTray.remove(trayIcon);
        }
    }

    public void addMenuItem(String title, MenuShortcut menuShortcut, ActionListener actionListener ) {
        MenuItem menuItem = new MenuItem(title, menuShortcut);
        menuItem.addActionListener(actionListener);
        addMenuItem(menuItem);
    }
    
    public void addMenuItem(MenuItem menuItem) {
        if (menuItem == null) {
            return;
        }
        this.menu.add(menuItem);
    }

    public void clearMenu() {
        this.menu.removeAll();
    }

    private MenuItem showMenuItem() {
        MenuItem menuItem = new MenuItem("Show");
        menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
        menuItem.addActionListener((ActionEvent e) -> {
            this.component.setVisible(true);
        });
        return menuItem;
    }

    private MenuItem hideMenuItem() {
        MenuItem menuItem = new MenuItem("Hide");
        menuItem.setShortcut(new MenuShortcut(KeyEvent.VK_H, false));
        menuItem.addActionListener((ActionEvent e) -> {
            this.component.setVisible(false);
        });
        return menuItem;
    }

    private boolean isExsits() {
        if (this.systemTray == null || this.trayIcon == null) {
            return false;
        }
        for (TrayIcon tray : this.systemTray.getTrayIcons()) {
            if (tray.equals(this.trayIcon)) {
                return true;
            }
        }
        return false;
    }
    
    private void mouseEvent() {
        this.trayIcon.addMouseListener( new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1 && e.getButton() == MouseEvent.BUTTON1) {
                    component.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
    

}
