    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.rts_assignment_final;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class RabbitMQ {
    
    static Soybean soybean;

    
    static byte[] byteArray = null;
    
    public static void producer(String exchange, byte[] object, String key, String exchange_type){

        ConnectionFactory factory = new ConnectionFactory();
        
        try(Connection con = factory.newConnection()){
            
            Channel chan = con.createChannel();
        
            if("f".equals(exchange_type)){
                chan.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
            }
            else if("d".equals(exchange_type)){
                chan.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
            }
            else{
                chan.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
            }
        
            chan.basicPublish(exchange, key, null, object);  
            
        } catch (IOException ex) {
            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void consumer(String exchange,String key, String exchange_type) throws IOException, TimeoutException{
        
        ConnectionFactory factory = new ConnectionFactory();
        
        Connection con = factory.newConnection();
            
        Channel chan = con.createChannel();
        
        if("f".equals(exchange_type)){
            chan.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
        }
        else if("d".equals(exchange_type)){
            chan.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        }
        else{
            chan.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
        }    
        
        String queue = chan.queueDeclare().getQueue();
        
        chan.queueBind(queue, exchange, key);
        
        DeliverCallback deliverCallback= (consumerTag, delivery)->{
                byteArray = delivery.getBody();
                
                //For worker
                if("Soybean Topic".equals(exchange)){
                    try {
                        Worker.collect();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                //For Shop Owner
                if("Soybean Direct".equals(exchange) && "Flour Key".equals(key)){
                    try {
                        ShopOwner.collectFlour();
                    } catch (IOException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if("Soybean Direct".equals(exchange) && "Residue Key".equals(key)){
                    try {
                        ShopOwner.collectResidue();
                    } catch (IOException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if("Soybean Direct".equals(exchange) && "Milk Key".equals(key)){
                    try {
                        ShopOwner.collectMilk();
                    } catch (IOException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
               
                //For Customer
                else if("Rabbit MQ Customer Topic".equals(exchange) && "*.FLOUR".equals(key)){
                    if(ShopOwner.type ==1){
                        try {
                        Customer_FnM.purchaseFlour();
                    } catch (IOException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    else{
                        try {
                        Customer_FnM.purchaseMilk();
                    } catch (IOException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }
                else if("Rabbit MQ Customer Topic".equals(exchange) && "FLOUR.*".equals(key)){
                    if(ShopOwner.type == 1){
                        try {
                            Customer_FnR.purchaseFlour();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        try {
                            Customer_FnR.purchaseResidue();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else if("Rabbit MQ Customer Topic".equals(exchange) && "RESIDUE.*".equals(key)){
                    if(ShopOwner.type == 3){
                        try {
                            Customer_RnM.purchaseResidue();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        try {
                            Customer_RnM.purchaseMilk();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else if("Rabbit MQ Customer Topic".equals(exchange) && "*.*.*".equals(key)){
                    if(ShopOwner.type == 1){
                        try {
                            Customer_E.purchaseFlour();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else if(ShopOwner.type == 3){
                        try {
                            Customer_E.purchaseResidue();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    else{
                        try {
                            Customer_E.purchaseMilk();
                        } catch (IOException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(RabbitMQ.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }; 
            
            chan.basicConsume(queue, true,deliverCallback, consumerTag->{});
    }   
    
    public static byte[] return_object(){
        
        return byteArray;
    }
    
    
    public static void purge_queue() throws IOException, TimeoutException{
        
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.newConnection();
        Channel chan = con.createChannel();
        chan.queueDelete("Rabbit MQ Customer Topic");
        chan.queueDelete("Soybean Direct");
        chan.queueDelete("Soybean Topic");
    }
    
}
