package com.tec02.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Administrator
 */
public class RequestParam {

    private final List<String[]> params;

    public static RequestParam builder() {
        return new RequestParam();
    }

    public RequestParam() {
        this.params = new ArrayList<>();
    }

    public RequestParam addParam(String key, Object... values) {
        if (key != null && values != null && values.length > 0) {
            for (Object value : values) {
                if(value == null){
                    continue;
                }
                this.params.add(new String[]{key, String.valueOf(value)});
            }
        }
        return this;
    }
    
    public RequestParam addParam(String key, Collection values) {
        return addParam(key, values.toArray());
    }

    public String toURL() {
        StringBuilder builder = new StringBuilder("?");
        for (String[] elem : this.params) {
            if (elem[1] != null) {
                builder.append(elem[0]);
                builder.append("=").append(elem[1]);
                builder.append("&");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString().trim();
    }

    @Override
    public String toString() {
        return toURL();
    }

}
