/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.common.API;

import com.alibaba.fastjson.JSONObject;
import java.util.Base64;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class JwtUtil {

    private JSONObject payload;
    private String JWT;

    public boolean setJWT(String jwt) {
        if (jwt == null || !jwt.matches(String.format("^%s.+\\..+\\..+$", BEARER))) {
            return false;
        }
        this.JWT = jwt;
        return (this.payload = extractAllClaims(jwt)) != null;
    }

    public String getJWT() {
        return String.valueOf(JWT);
    }

    public String extractUsername() {
        return getString("sub");
    }

    public String extractUserRole() {
        return getString("role");
    }

    public String getString(String key) {
        if (isLogout(key)) {
            return null;
        }
        return payload.getString(key);
    }

    public Integer getInteger(String key) {
        if (isLogout(key)) {
            return null;
        }
        return payload.getInteger(key);
    }

    private Long getLong(String key) {
        if (isLogout(key)) {
            return null;
        }
        return payload.getLong(key);
    }

    private boolean isLogout(String key) {
        return payload == null || key == null;
    }

    public String getString(String key, String defaultVal) {
        String val = getString(key);
        if (val == null) {
            return defaultVal;
        }
        return val;
    }

    public boolean isTokenValid() {
        return !isTokenExpired();
    }

    private boolean isTokenExpired() {
        Date date = extractExpiration();
        if (date == null) {
            return true;
        }
        return date.before(new Date());
    }

    private Date extractExpiration() {
        Long time = getLong("exp");
        if (time == null) {
            return null;
        }
        return new Date(time*1000);
    }

    private JSONObject extractAllClaims(String jwt) {
        try {
            String payload = jwt.split("\\.")[1];
            if (payload == null) {
                return null;
            }
            String payloadDecode = new String(Base64.getDecoder().decode(payload.getBytes()));
            return JSONObject.parseObject(payloadDecode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String BEARER = "Bearer ";

    public void logout() {
        this.JWT = null;
        this.payload = null;
    }

}
