use rand::{thread_rng, Rng};
use threadpool::ThreadPool;
use std::sync::Arc;
use crate::{Amiquip, CID, Flour, Milk, Residue};
use std::sync::mpsc::*;

pub struct Customer;
pub struct Customer_FnM;
pub struct Customer_FnR;
pub struct Customer_RnM;
pub struct Customer_E;



impl Customer {

    pub fn run(&self, mut CustomerID:i32, mut randomNumber:i32){

        let customers = ThreadPool::new(1);

        if(randomNumber ==0){
            customers.execute(move||{
                Customer_FnM.run(CustomerID);
            });
        }
        else if (randomNumber ==1) {
            customers.execute(move||{
                Customer_FnR.run(CustomerID);
            })
        }
        else if (randomNumber ==2) {
            customers.execute(move||{
                Customer_RnM.run(CustomerID);
            })
        }
        else{
            customers.execute(move||{
                Customer_E.run(CustomerID);
            })
        }
    }
}

impl Customer_FnM{

    pub fn run(&self, mut Customer_ID: i32){

        println!("Customer {}: Enters the shop! Looking for Flour & Milk", Customer_ID);
    }

    pub fn purchase(&self){

        let key = "*.FLOUR";

        let exchange_name = "Rabbit MQ Customer Topic";

        Amiquip::consumer(exchange_name,key,"t");
    }

    pub fn purchasesss(mut f:Flour){
        unsafe { println!("CUSTOMER {}: {} had been purchased!", CID, f); }
    }

    pub fn purchaseAll(mut flour:Flour, mut milk:Milk){
        unsafe { println!("CUSTOMER {}: {} & {} had been purchased!", CID, flour, milk); }
        unsafe { println!("CUSTOMER {}: leaves the shop!", CID); }
    }

}

impl Customer_FnR{

    pub fn run(&self, mut Customer_ID: i32){
        println!("Customer {}: Enters the shop! Looking for Flour & Residue", Customer_ID);
    }

    pub fn purchase(&self){

        let key = "FLOUR.*";

        let exchange_name = "Rabbit MQ Customer Topic";

        Amiquip::consumer(exchange_name,key,"t");
    }

    pub fn purchaseAll(mut flour:Flour, mut residue:Residue){
        unsafe { println!("CUSTOMER {}: {} & {} had been purchased!", CID, flour, residue); }
    }
}

impl Customer_RnM{

    pub fn run(&self, mut Customer_ID:i32){
        println!("Customer {}: Enters the shop! Looking for Residue & Milk", Customer_ID);
    }

    pub fn purchase(&self){

        let key = "RESIDUE.*";

        let exchange_name = "Rabbit MQ Customer Topic";

        Amiquip::consumer(exchange_name,key,"t");
    }

    pub fn purchaseAll(mut residue:Residue, mut milk:Milk){
        unsafe { println!("CUSTOMER {}: {} & {} had been purchased!", CID, residue, milk); }
    }
}

impl Customer_E{

    pub fn run(&self, mut Customer_ID:i32){
        println!("Customer {}: Enters the shop! Looking for Flour & Residue & Milk", Customer_ID);
    }

    pub fn purchase(&self){

        let key = "*.*.*";

        let exchange_name = "Rabbit MQ Customer Topic";

        Amiquip::consumer(exchange_name,key,"t");
    }

    pub fn purchaseAll(mut flour:Flour,mut residue:Residue, mut milk:Milk){
       unsafe { println!("CUSTOMER {}: {} & {} & {} had been purchased!", CID, flour, residue, milk); }
    }
}