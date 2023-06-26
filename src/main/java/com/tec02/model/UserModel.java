/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.model;

import com.tec02.common.JwtUtil;

/**
 *
 * @author Administrator
 */
public class UserModel {
    private JwtUtil jwtUtil;

    public UserModel() {
        this.jwtUtil = new JwtUtil();
    }

    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    public boolean setStringJWT(String stringJWT) {
        return this.jwtUtil.setJWT(stringJWT);
    }

    public String getStringJWT() {
        return this.jwtUtil.getJWT();
    }
    
    public boolean isAvlid(){
        return this.jwtUtil.isTokenValid();
    }
    
    public String getName(){
        return this.jwtUtil.extractUsername();
    }
    
    public String getRole(){
        return this.jwtUtil.extractUserRole();
    }

    public void logout() {
        this.jwtUtil.logout();
    }
    
}
