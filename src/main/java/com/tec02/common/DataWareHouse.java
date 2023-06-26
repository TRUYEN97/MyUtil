/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tec02.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;

/**
 *
 * @author Administrator
 */
public class DataWareHouse {

    private final JSONObject coreData;

    public DataWareHouse() {
        this.coreData = new JSONObject();
    }

    public DataWareHouse(JSONObject parseObject) {
        this.coreData = new JSONObject(parseObject);
    }

    public boolean putAll(Map data) {
        if (data != null) {
            this.coreData.putAll(data);
            return true;
        }
        return false;
    }

    public <T> List<T> getListJsonArray(String key) {
        return cvtArrays2List(getJSONArray(key));
    }

    public <T> List<T> cvtArrays2List(JSONArray array) {
        List<T> result = new ArrayList<>();
        if (isNull(array)) {
            return result;
        }
        for (Object elem : array) {
            if (elem != null) {
                result.add((T) elem);
            }
        }
        return result;
    }

    public String getString(String key) {
        if (coreData.containsKey(key)) {
            return coreData.getString(key);
        }
        return null;
    }

    public String[] getArrays(String key, String regex) {
        if (coreData.containsKey(key)) {
            String[] arr = coreData.getString(key).split(regex);
            String[] newArr = new String[arr.length];
            for (int index = 0; index < arr.length; index++) {
                newArr[index] = arr[index].trim();
            }
            return newArr;
        }
        return null;
    }

    public Integer getInteger(String key, int defaultValue) {
        Integer value = getInteger(key);
        return value == null ? defaultValue : value;
    }

    public Double getDouble(String key, double defaultValue) {
        Double value = getDouble(key);
        return value == null ? defaultValue : value;
    }

    public Integer getInteger(String key) {
        try {
            Integer value = coreData.getInteger(key);
            if (value != null) {
                return value;
            }
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Double getDouble(String key) {
        try {
            Double value = coreData.getDouble(key);
            if (value != null) {
                return value;
            }
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public JSONObject getJson(String key) {
        Object value = coreData.get(key);
        if (value instanceof JSONObject jSONObject) {
            return jSONObject;
        }
        return null;
    }

    public JSONObject toJson() {
        return (JSONObject) coreData.clone();
    }

    public JSONObject toJsonNonClone() {
        return coreData;
    }

    public JSONArray getJSONArray(String key) {
        return coreData.getJSONArray(key);
    }

    public Object put(String key, Object value) {
        return this.coreData.put(key, value);
    }

    public List<String> getListSlip(String key, String regex) {
        if (getArrays(key, regex) == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(getArrays(key, regex));
    }

    public List<JSONObject> getListJson(String key) {
        List<JSONObject> result = new ArrayList<>();
        var arr = coreData.getJSONArray(key);
        if (arr != null) {
            for (var object : arr) {
                result.add((JSONObject) object);
            }
        }
        return result;
    }

    public void clear() {
        this.coreData.clear();
    }

    public Long getLong(String key) {
        try {
            Long value = coreData.getLong(key);
            if (value != null) {
                return value;
            }
            return Long.valueOf(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            return coreData.getBoolean(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public Object get(String key) {
        return coreData.get(key);
    }

    public String getString(String key, String defaultVal) {
        String val = getString(key);
        return val == null ? defaultVal : val;
    }
}
