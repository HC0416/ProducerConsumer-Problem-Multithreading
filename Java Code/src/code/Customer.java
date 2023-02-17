/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rts_assignment_final;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Dell
 */
public class Customer implements Runnable{

    static ArrayList<Integer> ID_List = new ArrayList<>();
    static int CustomerID =0;
    static String bindindKey = "";
    
    ExecutorService ex = Executors.newFixedThreadPool(1);

    @Override
    public void run() {
        
        CustomerID++;
        ID_List.add(CustomerID);
        
        Random random = new Random(); 
        int randomNumber = random.nextInt(4);
        
        switch (randomNumber) {
            case 0 -> ex.submit(new Customer_FnM());
            case 1 -> ex.submit(new Customer_FnR());
            case 2 -> ex.submit(new Customer_RnM());
            default -> ex.submit(new Customer_E());
        }
    }
    
}


class Customer_FnM implements Runnable{
    
    static Flour flour = null;
    static Milk milk = null;

    @Override
    public void run() {
        System.out.println("\nCustomer " + Customer.CustomerID + " : Enters the Shop! Looking for Flour & Milk");
        Customer.bindindKey = "*.FLOUR";
    }
    
    public static void purchase() throws IOException, TimeoutException{
        String exchange = "Rabbit MQ Customer Topic";
        
        String key = "*.FLOUR";
        RabbitMQ.consumer(exchange, key, "t");
    }
    
    public static void purchaseFlour() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        flour = (Flour)Serialization.deserialize(object);
        ShopOwner.flourRack.remove(flour);
    }
    
    public static void purchaseMilk() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        milk = (Milk)Serialization.deserialize(object);
        ShopOwner.milkRack.remove(milk);
        
        if(flour != null && milk != null){
            System.out.println("Customer " + Customer.CustomerID + " : " + flour + " & "+ milk + " had been purchased!");
            System.out.println("Customer " + Customer.CustomerID + " : Leaves the Shop!");
            Customer.ID_List.remove(0);
        }
    }
    
}

class Customer_FnR implements Runnable{
    
    static Flour flour;
    static Residue residue;

    @Override
    public void run() {
        System.out.println("\nCustomer " + Customer.CustomerID + " : Enters the Shop! Looking for Flour & Residue");
        Customer.bindindKey = "FLOUR.*";
    }
    
    public static void purchase() throws IOException, TimeoutException{
        
        String exchange = "Rabbit MQ Customer Topic";
        
        String key = "FLOUR.*";
        RabbitMQ.consumer(exchange, key, "t");
    }
    
    public static void purchaseFlour() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        flour = (Flour)Serialization.deserialize(object);
        ShopOwner.flourRack.remove(flour);
        
    }
    
    public static void purchaseResidue() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        residue = (Residue)Serialization.deserialize(object);
        ShopOwner.residueRack.remove(residue);
        
        if(flour != null && residue != null){
            System.out.println("Customer " + Customer.CustomerID + " : " + flour + " & "+ residue + " had been purchased!");
            System.out.println("Customer " + Customer.CustomerID + " : Leaves the Shop!");
            Customer.ID_List.remove(0);
        }
        
    }

}

class Customer_RnM implements Runnable{
    
    static Residue residue;
    static Milk milk;

    @Override
    public void run() {
        System.out.println("\nCustomer " + Customer.CustomerID + " : Enters the Shop! Looking for Residue & Milk");
        Customer.bindindKey = "RESIDUE.*";
    }
    
    public static void purchase() throws IOException, TimeoutException{
        String exchange = "Rabbit MQ Customer Topic";
        
        String key = "RESIDUE.*";
        RabbitMQ.consumer(exchange, key, "t");
        
    }
    
    public static void purchaseResidue() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        residue = (Residue)Serialization.deserialize(object);
        ShopOwner.residueRack.remove(residue);
        
    }
    
    public static void purchaseMilk() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        milk = (Milk)Serialization.deserialize(object);
        ShopOwner.milkRack.remove(milk);
        
        if(residue != null && residue != null){
            System.out.println("Customer " + Customer.CustomerID + " : " + residue + " & "+ milk + " had been purchased!");
            System.out.println("Customer " + Customer.CustomerID + " : Leaves the Shop!");
            Customer.ID_List.remove(0);
        }
        
    }

}

class Customer_E implements Runnable{
    
    static Flour flour;
    static Milk milk;
    static Residue residue;

    @Override
    public void run() {
        System.out.println("\nCustomer " + Customer.CustomerID + " : Enters the Shop! Looking for Flour & Residue & Milk");
        Customer.bindindKey = "*.*.*";
    }
    
    public static void purchase() throws IOException, TimeoutException{
        String exchange = "Rabbit MQ Customer Topic";
        
        String key = "*.*.*";
        RabbitMQ.consumer(exchange, key, "t");
        
    }
    
    public static void purchaseFlour() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        flour = (Flour)Serialization.deserialize(object);
        ShopOwner.flourRack.remove(flour);
        
    }
    
    public static void purchaseResidue() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        residue = (Residue)Serialization.deserialize(object);
        ShopOwner.residueRack.remove(residue);
        
    }
    
    public static void purchaseMilk() throws IOException, ClassNotFoundException{
        
        byte[] object = RabbitMQ.return_object();
        milk = (Milk)Serialization.deserialize(object);
        ShopOwner.milkRack.remove(milk);
        
        if(flour != null && residue != null && milk !=null){
            System.out.println("Customer " + Customer.CustomerID + " : " + flour + " & "+ residue + " & "+ milk +" had been purchased!");
            System.out.println("Customer " + Customer.CustomerID + " : Leaves the Shop!");
            Customer.ID_List.remove(0);
        }
    }
}