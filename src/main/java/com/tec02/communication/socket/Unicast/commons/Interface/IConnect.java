/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Unicast.commons.Interface;

/**
 *
 * @author Administrator
 */
public interface IConnect extends IIsConnect, Idisconnect{

    boolean connect(String host, int port);

}
