/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.tec02.gui.guiInterface;

import com.tec02.Jmodel.IAction;
import com.tec02.API.RestAPI;
import com.tec02.gui.PopupMenuFilterAction;
import com.tec02.gui.panelGui.FilterUnit;
import com.tec02.gui.panelGui.MyFilter;

/**
 *
 * @author Administrator
 */
public interface IfileterBuilder {

    IfileterBuilder addFilter(String name,  IAction selectedAction, PopupMenuFilterAction focusGainedAction);

    IfileterBuilder addFilter(FilterUnit filterUnit);

    MyFilter build();

    public IfileterBuilder setAPI(RestAPI api);
}
