/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rts_assignment_final;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class ShopOwner implements Runnable{
    
    static Flour flour;
    static Residue residue;
    static Milk milk;
    
    static ArrayList<Flour> flourRack = new ArrayList<>();
    static ArrayList<Residue> residueRack = new ArrayList<>();
    static ArrayList<Milk> milkRack = new ArrayList<>();
    
    static int counter = 0;
    static int type = 0; //1.flour 2.milk 3.residue

    @Override
    public void run() {
        
        try {
            String exchange = "Rabbit MQ Customer Topic";
            
            String key_FnR = "FLOUR.RESIDUE";
            String key_FnM = "MILK.FLOUR";
            String key_RnM = "RESIDUE.MILK";
            String key_FnRnM = "FLOUR.RESIDUE.MILK";
            
            byte[] FlourByteArray = Serialization.getByteArray2(flour);
            byte[] ResidueByteArray = Serialization.getByteArray3(residue);
            byte[] MilkByteArray = Serialization.getByteArray4(milk);
            
            System.out.println("SHOP OWNER: Serving Customer " + Customer.ID_List.get(0) + "...");
            
            if("*.FLOUR".equals(Customer.bindindKey)){
                type = 1;
                RabbitMQ.producer(exchange, FlourByteArray, key_FnM, "t");
                type = 2;
                RabbitMQ.producer(exchange, MilkByteArray, key_FnM, "t");
            }
            else if("FLOUR.*".equals(Customer.bindindKey)){
                type = 1;
                RabbitMQ.producer(exchange, FlourByteArray, key_FnR, "t");
                type = 3;
                RabbitMQ.producer(exchange, ResidueByteArray, key_FnR, "t");
            }
            else if("RESIDUE.*".equals(Customer.bindindKey)){
                System.out.print        ("");
                type = 3;
                RabbitMQ.producer(exchange, ResidueByteArray, key_RnM, "t");
                type = 2;
                RabbitMQ.producer(exchange, MilkByteArray, key_RnM, "t");
            }
            else{
                type = 1;
                RabbitMQ.producer(exchange, FlourByteArray, key_FnRnM, "t");
                type = 3;
                RabbitMQ.producer(exchange, ResidueByteArray, key_FnRnM, "t");
                type = 2;
                RabbitMQ.producer(exchange, MilkByteArray, key_FnRnM, "t");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ShopOwner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void collect(){
        
        String exchange = "Soybean Direct";
        
        String key1 = "Flour Key";
        String key2 = "Residue Key";
        String key3 = "Milk Key";
        
        try {
            RabbitMQ.consumer(exchange, key1, "d");
            RabbitMQ.consumer(exchange, key2, "d");
            RabbitMQ.consumer(exchange, key3, "d");
            
        } catch (IOException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void collectFlour() throws IOException, ClassNotFoundException{
        
        counter++;
        
        byte[] object = RabbitMQ.return_object();
        
        flour = (Flour) Serialization.deserialize(object);
        
        flourRack.add(flour);
        
    }
    
    public static void collectResidue() throws IOException, ClassNotFoundException{
        
        counter++;
        
        byte[] object = RabbitMQ.return_object();
        
        residue = (Residue) Serialization.deserialize(object);
        
        residueRack.add(residue);
        
    }
    
    public static void collectMilk() throws IOException, ClassNotFoundException{
        
        counter++;
        
        byte[] object = RabbitMQ.return_object();
        
        milk = (Milk) Serialization.deserialize(object);
        
        milkRack.add(milk);
        
        if(counter == 45){
            System.out.println("\nSHOPOWNER: Purchased " + flour + " & " + residue + " & " + milk + " from the WORKER");
            counter = 0;
        }
    }
}
