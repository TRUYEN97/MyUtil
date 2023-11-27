/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.model;

import com.tec02.API.RestAPI;
import com.tec02.gui.panelGui.FilterUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import com.tec02.Jmodel.IAction;
import com.tec02.gui.PopupMenuFilterAction;

/**
 *
 * @author Administrator
 */
public class FilterModel {

    private final List<FilterUnit> FilterUnits;
    private RestAPI api;

    public FilterModel(RestAPI api) {
        this.api = api;
        this.FilterUnits = new ArrayList<>();
    }

    public FilterModel addFilter(String name, IAction selectedAction, PopupMenuFilterAction popupMenuListener) {
        name = name.trim();
        this.FilterUnits.add(new FilterUnit(name, selectedAction, popupMenuListener));
        return this;
    }
    
    public <T> T get(String name, Function<FilterUnit, T> function) {
        name = name.trim();
        FilterUnit filterUnit = getFilterUnit(name);
        if(filterUnit == null){
            return null;
        }
        return function.apply(filterUnit);
    }
    
    public String getSelected(String name) {
       return get(name, (t) -> t.getItemSelected());
    }

    public FilterModel addFilter(FilterUnit filterUnit) {
         this.FilterUnits.add(filterUnit);
         return this;
    }

    public int count() {
        return this.FilterUnits.size();
    }

    public Collection<FilterUnit> getFilter() {
        return this.FilterUnits;
    }
    
    public boolean isContain(String name){
        return getFilterUnit(name) != null;
    }

    public FilterUnit getFilterUnit(String name) {
        for (FilterUnit filterUnit : FilterUnits) {
            if (filterUnit.getLabelName().equalsIgnoreCase(name)) {
                return filterUnit;
            }
        }
        return null;
    }

    public void setAPI(RestAPI api) {
        this.api = api;
    }

    public RestAPI getAPI() {
        return api;
    }
    

}
