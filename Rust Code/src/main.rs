mod Amiquip;
mod Farmer;
mod Worker;
mod ShopOwner;
mod Customer;

use bma_benchmark::benchmark;
use bma_benchmark::{staged_benchmark_finish_current, staged_benchmark_print_for, staged_benchmark_start};

use amiquip::Connection;
use scheduled_thread_pool::ScheduledThreadPool;
use std::sync::mpsc::*;
use std::time::Duration;
use rand::{thread_rng, Rng};
use serde::{Deserialize, Serialize};
use std::fmt;
use crate::Customer::Customer_E;

static mut CID: i32 = 0;

fn main() {

    Amiquip::purge_queue();


    benchmark();

/*    let n = 5;
    staged_benchmark_start!("Process of Soybean");
    for _ in 0..n {
        test();
    }
    staged_benchmark_finish_current!(n);
    staged_benchmark_print_for!("Process of Soybean");*/
}

pub fn test(){

    let farmer = Farmer::farmer;
    let worker = Worker::worker;
    let shopOwner = ShopOwner::shopOwner;
    let shopOwner2 = ShopOwner::shopOwner;
    let customer = Customer::Customer;
    let customer_fnm = Customer::Customer_FnM;
    let customer_fnr = Customer::Customer_FnR;
    let customer_rnm = Customer::Customer_RnM;
    let customer_e = Customer::Customer_E;

    let executor = ScheduledThreadPool::new(3);
    let (tx, rx): (Sender<u8>, Receiver<u8>) = channel();
    let (tx2, rx2): (Sender<u8>, Receiver<u8>) = channel();
    let (tx3, rx3): (Sender<u8>, Receiver<u8>) = channel();
    let (sender, receiver): (Sender<u8>, Receiver<u8>) = channel();
    let (sender2, receiver2): (Sender<i32>, Receiver<i32>) = channel();
    let killswitch = std::sync::Arc::new(std::sync::Mutex::new(false));
    let killswitch2 = killswitch.clone();
    let killswitch3 = killswitch.clone();
    let killswitch4 = killswitch.clone();

    let mut CustomerID = 0;

    let n = 100;
    executor.execute_at_fixed_rate(Duration::from_secs(1),Duration::from_secs(5), move||{
        farmer.run();
        if *(killswitch2.lock().unwrap()) {
            panic!("FARMER: End!");
        }
        //tx.send(1).unwrap();
    });

    executor.execute(move||{
        worker.run();
    });

    executor.execute_at_fixed_rate(Duration::from_secs(4), Duration::from_secs(3), move||{
        let mut random = rand::thread_rng();
        let randomNumber = random.gen_range(0..4);
        sender.send(randomNumber).unwrap();

        CustomerID +=1;
        unsafe { CID = CustomerID; }

        sender2.send(CustomerID).unwrap();
        customer.run(CustomerID, randomNumber.into());


        if *(killswitch3.lock().unwrap()) {
            panic!("CUSTOMER: End!");
        }
        tx.send(1).unwrap();
    });

    executor.execute_at_fixed_rate(Duration::from_secs(7), Duration::from_secs(3), move||{
        let mut bindingKey = "";
        let mut rNumber = receiver.recv().unwrap();
        let mut cID = receiver2.recv().unwrap();

        if (rNumber == 0){
            bindingKey = "*.FLOUR";
        }
        else if(rNumber == 1){
            bindingKey = "FLOUR.*";
        }
        else if(rNumber ==2){
            bindingKey = "RESIDUE.*";
        }
        else if(rNumber ==3){
            bindingKey = "*.*.*";
        }

        shopOwner2.run(bindingKey.to_string(), cID);

        if *(killswitch4.lock().unwrap()) {
            panic!("SHOP OWNER: End!");
        }
        //tx3.send(1).unwrap();
    });

    executor.execute(move||{
        shopOwner.collect();
    });
    for (count, _) in rx.iter().enumerate() {
        if count > 10 {
            *(killswitch.lock().unwrap()) = true;
        }
    }

    customer_fnm.purchase();
    customer_fnr.purchase();
    customer_fnm.purchase();
    customer_rnm.purchase();
    customer_e.purchase();

/*    for (count, _) in rx2.iter().enumerate() {
        if count > 1 {
            *(killswitch.lock().unwrap()) = true;
        }
    }

    for (count, _) in rx3.iter().enumerate() {
        if count > 1 {
            *(killswitch.lock().unwrap()) = true;
        }
    }*/

}

pub fn benchmark(){
    let farmer = Farmer::farmer;
    let worker = Worker::worker;
    let shopOwner = ShopOwner::shopOwner;
    let shopOwner2 = ShopOwner::shopOwner;
    let customer = Customer::Customer;
    let customer_fnm = Customer::Customer_FnM;
    let customer_fnr = Customer::Customer_FnR;
    let customer_rnm = Customer::Customer_RnM;
    let customer_e = Customer::Customer_E;

    let executor = ScheduledThreadPool::new(3);
    let (tx, rx): (Sender<u8>, Receiver<u8>) = channel();
    let (tx2, rx2): (Sender<u8>, Receiver<u8>) = channel();
    let (tx3, rx3): (Sender<u8>, Receiver<u8>) = channel();
    let (sender, receiver): (Sender<u8>, Receiver<u8>) = channel();
    let (sender2, receiver2): (Sender<i32>, Receiver<i32>) = channel();
    let killswitch = std::sync::Arc::new(std::sync::Mutex::new(false));
    let killswitch2 = killswitch.clone();
    let killswitch3 = killswitch.clone();
    let killswitch4 = killswitch.clone();

    let mut CustomerID = 0;

    let n = 100;
    executor.execute_at_fixed_rate(Duration::from_secs(1),Duration::from_secs(5), move||{
        farmer.run();
        if *(killswitch2.lock().unwrap()) {
            panic!("FARMER: End!");
        }
        tx.send(1).unwrap();
    });

    executor.execute(move||{
        worker.run();
    });

    executor.execute_at_fixed_rate(Duration::from_secs(6), Duration::from_secs(3), move||{
        let mut random = rand::thread_rng();
        let randomNumber = random.gen_range(0..4);
        sender.send(randomNumber).unwrap();

        CustomerID +=1;
        unsafe { CID = CustomerID; }

        sender2.send(CustomerID).unwrap();
        customer.run(CustomerID, randomNumber.into());


        if *(killswitch3.lock().unwrap()) {
            panic!("CUSTOMER: End!");
        }
        tx2.send(1).unwrap();
    });

    executor.execute_at_fixed_rate(Duration::from_secs(7), Duration::from_secs(3), move||{
        let mut bindingKey = "";
        let mut rNumber = receiver.recv().unwrap();
        let mut cID = receiver2.recv().unwrap();

        if (rNumber == 0){
            bindingKey = "*.FLOUR";
        }
        else if(rNumber == 1){
            bindingKey = "FLOUR.*";
        }
        else if(rNumber ==2){
            bindingKey = "RESIDUE.*";
        }
        else if(rNumber ==3){
            bindingKey = "*.*.*";
        }

        shopOwner2.run(bindingKey.to_string(), cID);

        if *(killswitch4.lock().unwrap()) {
            panic!("SHOP OWNER: End!");
        }
        tx3.send(1).unwrap();
    });

    executor.execute(move||{
        shopOwner.collect();
    });

    customer_fnm.purchase();
    customer_fnr.purchase();

    customer_rnm.purchase();


    customer_e.purchase();


    for (count, _) in rx.iter().enumerate() {
        if count > 1 {
            *(killswitch.lock().unwrap()) = true;
        }
    }

    for (count, _) in rx2.iter().enumerate() {
        if count > 1 {
            *(killswitch.lock().unwrap()) = true;
        }
    }

    for (count, _) in rx3.iter().enumerate() {
        if count > 1 {
            *(killswitch.lock().unwrap()) = true;
        }
    }
}

#[derive(Debug, Serialize)]
pub struct Soybean{
    name: String
}

#[derive(Clone, Debug, Serialize, PartialEq)]
pub struct Flour{
    name: String
}

#[derive(Clone, Debug, Serialize, PartialEq)]
pub struct Residue{
    name: String
}

#[derive(Clone, Debug, Serialize, PartialEq)]
pub struct Milk{
    name: String
}

impl fmt::Display for Soybean {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}

impl fmt::Display for Flour {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}

impl fmt::Display for Residue {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}

impl fmt::Display for Milk {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.name)
    }
}