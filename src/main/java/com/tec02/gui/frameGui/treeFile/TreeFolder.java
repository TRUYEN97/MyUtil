/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui.treeFile;

import com.tec02.event.IAction;
import com.tec02.gui.frameGui.Component.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Administrator
 */
public class TreeFolder {

    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private PopupMenu menu;
    private final FileNode root;
    private Thread reloadThread;
    private IAction<MouseEvent> doubleClickAction;

    public TreeFolder(JTree tree, boolean rootVisible) {
        this.tree = tree;
        this.menu = new PopupMenu();
        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (menu != null && e.getButton() == MouseEvent.BUTTON3) {
                    menu.show(e);
                } else if (e.getButton() == MouseEvent.BUTTON1) {
                    if (tree.getSelectionCount() > 0
                            && tree.getRowForLocation(e.getPoint().x, e.getPoint().y) == -1
                            && (!e.isControlDown() || e.getClickCount() > 1)) {
                        tree.clearSelection();
                        return;
                    }
                    if (e.getClickCount() > 1) {
                        if (doubleClickAction != null) {
                            doubleClickAction.action(e);
                        }
                    }
                }
            }
        });
        setRootVisible(rootVisible);
        this.treeModel = (DefaultTreeModel) tree.getModel();
        this.root = new FileNode("", null, true);
        this.treeModel.setRoot(this.root);
        this.menu.addItemMenu("Refresh", (e) -> {
           refresh();
        });
    }

    public void setDoubleClickAction(IAction<MouseEvent> doubleClickAction) {
        this.doubleClickAction = doubleClickAction;
    }

    public IAction<MouseEvent> getDoubleClickAction() {
        return doubleClickAction;
    }

    public final void setRootVisible(boolean rootVisible) {
        this.tree.setRootVisible(rootVisible);
    }

    public PopupMenu getPopupMenu() {
        return menu;
    }

    public void setPopupMenu(PopupMenu popupMenu) {
        this.menu = popupMenu;
    }

    public void addFile(String dir, String fileName, Map<String, Object> data) {
        if (fileName == null) {
            return;
        }
        List<String> parents = getPathElements(dir);
        FileNode folder = getFolderNode(parents, root);
        folder.add(new FileNode(fileName, data, false));
        reloadNode(folder);
    }

    private void reloadNode(FileNode folder) {
        if (this.reloadThread != null && this.reloadThread.isAlive()) {
            return;
        }
        this.reloadThread = new Thread() {
            @Override
            public void run() {
                treeModel.reload(folder);
            }
        };
        this.reloadThread.start();
    }

    /*
    [g,h,j]
    root,g,h,j
     */
    private FileNode getFolderNode(List<String> parents, FileNode root) {
        if (parents == null || parents.isEmpty()) {
            return root;
        }
        int level = root.getLevel();
        if (level >= parents.size()) {
            return root;
        }
        String name = parents.get(level);
        for (Enumeration<TreeNode> iterator = root.children(); iterator.hasMoreElements();) {
            FileNode node = (FileNode) iterator.nextElement();
            if (node.isLeaf()) {
                continue;
            }
            if (node.equalsName(name)) {
                return getFolderNode(parents, node);
            }
        }
        FileNode newNode = new FileNode(name, null, true);
        root.addFileNode(newNode);
        return getFolderNode(parents, newNode);
    }

    private List<String> getPathElements(String path) {
        if (path == null || path.isBlank()) {
            return List.of();
        }
        Path parent = Path.of(path).getParent();
        if (parent == null) {
            return List.of(path);
        } else {
            List<String> rs = new ArrayList<>();
            rs.addAll(getPathElements(parent.toString()));
            rs.add(Path.of(path).getFileName().toString());
            return rs;
        }
    }

    public void clear() {
        try {
            this.root.removeAllChildren();
            reloadNode(root);
            Thread.sleep(200);
        } catch (InterruptedException ex) {
        }
    }

    public FileNode getNodeSelected() {
        var path = this.tree.getSelectionPath();
        if (path == null) {
            return null;
        }
        return (FileNode) path.getLastPathComponent();
    }

    public Object getNodeSelectedValue(String key) {
        FileNode fileNode = this.getNodeSelected();
        if (fileNode == null) {
            return null;
        }
        return fileNode.getData(key);
    }

    public void refresh() {
         this.reloadNode(root);
    }

    public class FileNode extends DefaultMutableTreeNode {

        private final boolean isFolder;
        private final Map<String, Object> data;

        public FileNode(String name, Map<String, Object> data, boolean isFolder) {
            super(name.trim(), isFolder);
            this.isFolder = isFolder;
            if(data != null){
                this.data = data;
            }else{
                this.data = Map.of();
            }
        }

        public Path getNodePath() {
            Object[] elements = this.getUserObjectPath();
            String[] path = new String[elements.length];
            for (int i = 0; i < elements.length; i++) {
                path[i] = String.valueOf(elements[i]);
            }
            return Paths.get("", path);
        }

        public Map<String, Object> getData() {
            return data;
        }

        public Object getData(String key) {
            return data.get(key);
        }

        public Object getData(String key, Object defaultValue) {
            return data.getOrDefault(key, defaultValue);
        }

        public void addFileNode(FileNode newChild) {
            add(newChild);
        }

        @Override
        public void add(MutableTreeNode newChild) {
            FileNode node = (FileNode) newChild;
            if (isLeaf()) {
                return;
            }
            super.add(node);
        }

        @Override
        public boolean isLeaf() {
            return !isFolder();
        }

        public String getNodeName() {
            return getUserObject().toString();
        }

        @Override
        public void setUserObject(Object userObject) {
            super.setUserObject(userObject.toString());
        }

        public boolean isFolder() {
            return isFolder;
        }

        public boolean isFile() {
            return !isFolder;
        }

        private boolean equalsName(String name) {
            return getNodeName().equalsIgnoreCase(name);
        }

    }
}
