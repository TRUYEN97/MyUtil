/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.common.RestAPI;
import com.tec02.event.PopupMenuFilterAction;
import com.tec02.model.FilterModel;
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
        super(new FilterModel(api)
                .addFilter(PRODUCT, null, new PopupMenuFilterAction() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        getMyFilter().getListFromServerWithFilter(getFilterUnit(), urlProduct);
                    }
                })
                .addFilter(STATION, null, new PopupMenuFilterAction() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        getMyFilter().getListFromServerWithFilter(getFilterUnit(), urlStation);
                    }
                })
                .addFilter(LINE, null, new PopupMenuFilterAction() {
                    @Override
                    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                        getMyFilter().getListFromServerWithFilter(getFilterUnit(), urlLine);
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

    public String getProduct() {
        return this.model.get(LocationFilter.PRODUCT, (t) -> t.getItemSelected());
    }

    public String getStation() {
        return this.model.get(LocationFilter.STATION, (t) -> t.getItemSelected());
    }

    public String getLine() {
        return this.model.get(LocationFilter.LINE, (t) -> t.getItemSelected());
    }
}
