/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rts_assignment_final;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class Farmer implements Runnable{

    @Override
    public void run() {
        System.out.println("\nFARMER: Collecting Soybeans...");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Farmer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Soybean soybean = new Soybean();
        soybean.Name = "soybean";
        
        String exchange = "Soybean Topic";
        
        byte[] SoybeanByteArray = null;
        
        try {
            SoybeanByteArray = Serialization.getByteArray(soybean);
        } catch (IOException ex) {
            Logger.getLogger(Farmer.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("FARMER: Sending " + soybean + " to the WORKER");
        
        RabbitMQ.producer(exchange, SoybeanByteArray, "", "f");
        
        
    }
    
}
