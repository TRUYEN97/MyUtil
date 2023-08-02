/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.common.RestAPI;
import com.tec02.gui.PopupMenuFilterAction;
import com.tec02.gui.model.FilterModel;
import java.util.List;
import javax.swing.event.PopupMenuEvent;

/**
 *
 * @author Administrator
 */
public class LocationFilter extends MyFilter {

    public static final String LINE = "Line";
    public static final String STATION = "Station";
    public static final String PRODUCT = "Product";
    public final String urlProduct;
    public final String urlStation;
    public final String urlLine;

    public LocationFilter(RestAPI api, String urlProduct, String urlStation, String urlLine) {
        super(api);
        update(new FilterModel(api)
                .addFilter(PRODUCT, null, new PopupMenuFilterAction() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        getListFromServerWithFilter(getFilterUnit(), urlProduct);
                    }
                })
                .addFilter(STATION, null, new PopupMenuFilterAction() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        getListFromServerWithFilter(getFilterUnit(), urlStation);
                    }
                })
                .addFilter(LINE, null, new PopupMenuFilterAction() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        getListFromServerWithFilter(getFilterUnit(), urlLine);
                    }
                }));
        this.urlProduct = urlProduct;
        this.urlStation = urlStation;
        this.urlLine = urlLine;
    }

    public void refresh() {
        getListFromServerWithFilter(PRODUCT, urlProduct);
        getListFromServerWithFilter(STATION, urlStation);
        getListFromServerWithFilter(LINE, urlLine);
    }

    public String getProductSelection() {
        return this.model.get(LocationFilter.PRODUCT, (t) -> t.getItemSelected());
    }

    public String getStationSelection() {
        return this.model.get(LocationFilter.STATION, (t) -> t.getItemSelected());
    }

    public String getLineSelection() {
        return this.model.get(LocationFilter.LINE, (t) -> t.getItemSelected());
    }

    public List<String> getProductItems() {
        return this.model.get(LocationFilter.PRODUCT, (t) -> t.getListItems());
    }

    public List<String> getStationItems() {
        return this.model.get(LocationFilter.STATION, (t) -> t.getListItems());
    }

    public List<String> getLineItems() {
        return this.model.get(LocationFilter.LINE, (t) -> t.getListItems());
    }
}
