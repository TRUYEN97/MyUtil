/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author Administrator
 */
public class JsonBodyAPI {
    private final JSONObject body;

    public static JsonBodyAPI builder(){
        return  new JsonBodyAPI();
    }
    
    private JsonBodyAPI() {
        this.body = new JSONObject();
    }
    
    public JsonBodyAPI put(String key, Object value){
        this.body.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return body.toJSONString();
    }
    
    public String toJSONString(){
        return this.body.toJSONString();
    }
}
