package com.mycompany.rts_assignment_final;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

/**
 *
 * @author Dell
 */
public class Main {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException{
        
        RabbitMQ.purge_queue();
        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        executor.scheduleAtFixedRate(new Farmer(),1, 5,TimeUnit.SECONDS);
        executor.schedule(new Worker(), 0, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new ShopOwner(),7, 3,TimeUnit.SECONDS); 
        executor.scheduleAtFixedRate(new Customer(),6, 3,TimeUnit.SECONDS);
      
        ShopOwner.collect();
        Customer_FnR.purchase();
        Customer_FnM.purchase();
        Customer_RnM.purchase();        
        Customer_E.purchase();
          
        
       //org.openjdk.jmh.Main.main(args);
    }
    
    
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Warmup(iterations = 3) //Set the warm up to 3
    @Measurement(iterations = 5, time =3, timeUnit = TimeUnit.SECONDS) //Set only 5 iterations, run every iteratios for 3 seconds
    @Fork(1)
    public static void test() throws IOException, TimeoutException, InterruptedException{
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        CountDownLatch latch = new CountDownLatch(1);

        
        executor.scheduleAtFixedRate(new Farmer(), 1, 5, TimeUnit.SECONDS);
        executor.schedule(new Worker(), 0, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new ShopOwner(),7, 3,TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(new Customer(),6, 3,TimeUnit.SECONDS);
        
        ShopOwner.collect();
        Customer_FnM.purchase();
        Customer_FnR.purchase();
        Customer_RnM.purchase();
        Customer_E.purchase();
        
        executor.scheduleAtFixedRate(() -> {
        if (Customer.CustomerID > 10) {
          latch.countDown();
        }
        }, 0, 1, TimeUnit.SECONDS);
        latch.await();
        executor.shutdownNow();
        
    }
    
}

class Soybean implements Serializable{

    public String Name;
    
    public String getName(){
        return Name;
    }
    
    public void setName(String Name){
        this.Name = Name;
    }
    
    public String toString(){
        return Name;
    }
}

class Flour implements Serializable{

    public String Name;
    
    public String getName(){
        return Name;
    }
    
    public void setName(String Name){
        this.Name = Name;
    }
    
    public String toString(){
        return Name;
    }
}

class Residue implements Serializable{

    public String Name;
    
    public String getName(){
        return Name;
    }
    
    public void setName(String Name){
        this.Name = Name;
    }
    
    public String toString(){
        return Name;
    }
}

class Milk implements Serializable{

    public String Name;
    
    public String getName(){
        return Name;
    }
    
    public void setName(String Name){
        this.Name = Name;
    }
    
    public String toString(){
        return Name;
    }
}
