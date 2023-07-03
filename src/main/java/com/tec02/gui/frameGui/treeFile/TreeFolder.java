/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.frameGui.treeFile;

import com.tec02.event.IAction;
import com.tec02.gui.frameGui.Component.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
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
    private IAction<MouseEvent> doubleClickAction;

    public TreeFolder(JTree tree, boolean rootVisible) {
        this.tree = tree;
        this.menu = new PopupMenu();
        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (doubleClickAction != null
                        && e.getClickCount() > 1
                        && e.getButton() == MouseEvent.BUTTON1) {
                    doubleClickAction.action(e);
                }
                if (menu != null && e.getButton() == MouseEvent.BUTTON3) {
                    menu.show(e);
                }

            }
        });
        setRootVisible(rootVisible);
        this.treeModel = (DefaultTreeModel) tree.getModel();
        this.root = new FileNode(".", true);
        this.treeModel.setRoot(this.root);
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

    public void addFile(String dir, String fileName) {
        List<String> parents = getPathElements(Path.of(dir));
        FileNode folder = getFolderNode(parents, root);
        folder.add(new FileNode(fileName, false));
    }

    /*
    [g,h,j]
    root,g,h,j
     */
    private FileNode getFolderNode(List<String> parents, FileNode root) {
        int level = root.getLevel();
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
        FileNode newNode = new FileNode(name, true);
        root.add(newNode);
        if (level < parents.size() - 1) {
            return getFolderNode(parents, newNode);
        }
        return newNode;
    }

    private List<String> getPathElements(Path path) {
        Path parent = path.getParent();
        if (parent == null) {
            return List.of(path.toString());
        } else {
            List<String> rs = new ArrayList<>();
            rs.addAll(getPathElements(parent));
            rs.add(path.getFileName().toString());
            return rs;
        }
    }

    public void clear() {
        this.root.removeAllChildren();
    }

    private class FileNode extends DefaultMutableTreeNode {

        private static final String EMPTY = "Empty!";
        private final boolean isFolder;

        public FileNode(String name, boolean isFolder) {
            super(name.trim(), isFolder);
            this.isFolder = isFolder;
            if (isFolder) {
                super.add(new FileNode(EMPTY, false));
            }
        }

        public void addFileNode(FileNode newChild) {
            add(newChild);
        }

        @Override
        public void add(MutableTreeNode newChild) {
            FileNode node = (FileNode) newChild;
            if (isLeaf()) {
                return;
            } else if (getChildCount() == 1) {
                FileNode chiled = (FileNode) getChildAt(0);
                if (chiled.equalsName(EMPTY)) {
                    newChild.remove(0);
                }
            }
            super.add(node);
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
