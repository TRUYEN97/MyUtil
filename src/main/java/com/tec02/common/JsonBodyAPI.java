/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONObject;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class JsonBodyAPI {
    private final JSONObject body;

    public static JsonBodyAPI builder(){
        return new JsonBodyAPI();
    }
    
    public JsonBodyAPI() {
        this.body = new JSONObject();
    }
    
    public JsonBodyAPI put(String key, Object value){
        this.body.put(key, value);
        return this;
    }
    
    public Set<String> getKeySet(){
        return this.body.keySet();
    }
    
    public boolean isEmpty(){
        return this.body.isEmpty();
    }
    
    public int size(){
        return this.body.size();
    }

    @Override
    public String toString() {
        return body.toJSONString();
    }
    
    public String toJSONString(){
        return this.body.toJSONString();
    }

    public String getString(String key) {
        return this.body.getString(key);
    }
}
