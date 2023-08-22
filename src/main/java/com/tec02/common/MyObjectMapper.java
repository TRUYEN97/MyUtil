/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MyObjectMapper {

    protected ObjectMapper objectMapper;

    private MyObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    }

    public static <T> List<T> convertToList(JSONArray objects, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (objects == null || objects.isEmpty()) {
            return list;
        }
        for (Object object : objects) {
            list.add(convertValue(object, clazz));
        }
        return list;
    }

    public static <T> T convertValue(Object object, Class<T> clazz) {
        MyObjectMapper objectMapper = new MyObjectMapper();
        return objectMapper.objectMapper.convertValue(object, clazz);
    }

    public static <T> T convertValue(Object object, JavaType clazz) {
        MyObjectMapper objectMapper = new MyObjectMapper();
        return objectMapper.objectMapper.convertValue(object, clazz);
    }

}
