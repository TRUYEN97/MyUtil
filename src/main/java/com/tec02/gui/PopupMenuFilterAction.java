/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui;

import com.tec02.gui.panelGui.FilterUnit;
import com.tec02.gui.panelGui.MyFilter;
import javax.swing.event.PopupMenuEvent;

/**
 *
 * @author Administrator
 */
public class PopupMenuFilterAction implements javax.swing.event.PopupMenuListener{

    private FilterUnit filterUnit;
    
    private MyFilter myFilter;
    
    public PopupMenuFilterAction(){
        
    }

    public MyFilter getMyFilter() {
        return myFilter;
    }

    public void setMyFilter(MyFilter myFilter) {
        this.myFilter = myFilter;
    }
    
    public FilterUnit getFilterUnit() {
        return filterUnit;
    }

    public void setFilterUnit(FilterUnit filterUnit) {
        this.filterUnit = filterUnit;
    }
    
    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e) {
    }
    
}
