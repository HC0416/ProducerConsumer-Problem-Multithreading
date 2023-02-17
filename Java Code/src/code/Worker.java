/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rts_assignment_final;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class Worker implements Runnable{
    
    static Soybean soybean;

    @Override
    public void run() {
        
        String exchange = "Soybean Topic";
        
        try {
            RabbitMQ.consumer(exchange, "", "f");
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void collect() throws InterruptedException, IOException{
       
       byte[] object = RabbitMQ.return_object();
       
       try {
            soybean = (Soybean) Serialization.deserialize(object);
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("\nWORKER: " + soybean + " had been collected!");
        manufacturer();
       
    }
    
    public static void manufacturer() throws InterruptedException, IOException{
        
        Flour flour = new Flour();
        Residue residue = new Residue();
        Milk milk = new Milk();
        
        flour.Name = "flour";
        residue.Name = "residue";
        milk.Name = "milk";
        
        System.out.println("\nWORKER: Begin Manufacturing Soybeans...");
        
        Thread.sleep(1000);
        
        System.out.println("WORKER: Soybean " + flour + " is manufactured");
        System.out.println("WORKER: Soybean " + residue + " is manufactured");
        System.out.println("WORKER: Soybean " + milk + " is manufactured");

        sell_product(flour,residue,milk);
    }
    
    public static void sell_product(Flour flour, Residue residue, Milk milk) throws IOException{
        
        String exchange = "Soybean Direct";
        
        String key1 = "Flour Key";
        String key2 = "Residue Key";
        String key3 = "Milk Key";
        
        byte[] flourByteArray = null;
        byte[] residueByteArray = null;
        byte[] milkByteArray = null;
        
        flourByteArray = Serialization.getByteArray2(flour);
        residueByteArray = Serialization.getByteArray3(residue);
        milkByteArray = Serialization.getByteArray4(milk);
        
        
        for(int i=0 ; i<15 ; i++){
            RabbitMQ.producer(exchange, flourByteArray, key1, "d");
            RabbitMQ.producer(exchange, residueByteArray, key2, "d");
            RabbitMQ.producer(exchange, milkByteArray, key3, "d");
        }
        
        
    }
}
