/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tec02.gui;

import javax.swing.JPanel;

/**
 *
 * @author Administrator
 */
public abstract class Panelupdate extends JPanel{
    
    public void update(){
        var a = this.getParent();
        if(a != null){
            this.setBackground(a.getBackground());
        }
    }
}
