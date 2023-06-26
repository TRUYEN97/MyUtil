/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui.panelGui;

import com.tec02.common.RestAPI;
import com.tec02.model.FilterModel;

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
                .addFilter(PRODUCT, null, null)
                .addFilter(STATION, null, null)
                .addFilter(LINE, null, null));
        this.urlProduct = urlProduct;
        this.urlStation = urlStation;
        this.urlLine = urlLine;
        getFilterUnit(PRODUCT).setFocusGainedAction((input) -> {
            getListFromServerWithFilter(urlProduct, input);
        });
        getFilterUnit(STATION).setFocusGainedAction((input) -> {
            getListFromServerWithFilter(urlStation, input);
        });
        getFilterUnit(LINE).setFocusGainedAction((input) -> {
            getListFromServerWithFilter(urlLine, input);
        });
    }

    public void refresh() {
        getListFromServerAddToFilter(PRODUCT, urlProduct);
        getListFromServerAddToFilter(STATION, urlStation);
        getListFromServerAddToFilter(LINE, urlLine);
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
