/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.model;

import com.tec02.common.RestAPI;
import com.tec02.gui.guiInterface.IfileterBuilder;
import com.tec02.gui.panelGui.FilterUnit;
import com.tec02.gui.panelGui.MyFilter;
import com.tec02.gui.IAction;
import com.tec02.gui.PopupMenuFilterAction;

/**
 *
 * @author Administrator
 */
public class MyFilterBuilder implements IfileterBuilder {

    protected final FilterModel model;

    public MyFilterBuilder(RestAPI aPI) {
        this.model = new FilterModel(aPI);
    }

    @Override
    public IfileterBuilder addFilter(String name, IAction selectedAction, PopupMenuFilterAction focusGainedAction) {
        this.model.addFilter(name, selectedAction, focusGainedAction);
        return this;
    }

    @Override
    public IfileterBuilder addFilter(FilterUnit filterUnit) {
        this.model.addFilter(filterUnit);
        return this;
    }

    @Override
    public MyFilter build() {
        return new MyFilter(this.model);
    }

    @Override
    public IfileterBuilder setAPI(RestAPI api) {
        this.model.setAPI(api);
        return this;
    }

}
