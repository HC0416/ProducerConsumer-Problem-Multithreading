use amiquip::{Connection, ConsumerMessage, ConsumerOptions, QueueDeclareOptions, Exchange, Publish, Result, ExchangeType, ExchangeDeclareOptions};

use std::time::Duration;
use crate::{Amiquip, Flour, Milk, Residue, Soybean};

pub struct worker;

impl worker {

    pub fn run(&self){

        let exchange_name = "Soybean Topic";

        Amiquip::consumer(exchange_name, "" , "f");

    }

    pub fn collect(mut soybean:Soybean){

        println!("WORKER: {} is received!", soybean.to_string());
        Self::manufacturer();
    }

    fn manufacturer(){

        let mut flour = Flour{name: String::from("flour")};
        let mut residue = Residue{name: String::from("residue")};
        let mut milk = Milk{name: String::from("milk")};

        //println!("\nWORKER: Begin Manufacuturing Soybeans...");
        std::thread::sleep(Duration::from_secs(2));

        println!("WORKER: Soybean {} is produced!", flour.to_string());
        println!("WORKER: Soybean {} is produced!", residue.to_string());
        println!("WORKER: Soybean {} is produced!", milk.to_string());

        Self::sell_product(flour, residue, milk);
    }

    fn sell_product(mut flour:Flour, mut residue:Residue, mut milk:Milk){

        let exchange_name = "Soybean Direct";

        let key1 = "Flour key";
        let key2 = "Residue Key";
        let key3 = "Milk key";

        let FlourBytes = bincode::serialize(&flour).unwrap();
        let ResidueBytes = bincode::serialize(&residue).unwrap();
        let milkBytes = bincode::serialize(&milk).unwrap();

        Amiquip::producer(exchange_name,FlourBytes,key1,"d");
        Amiquip::producer(exchange_name,ResidueBytes,key2,"d");
        Amiquip::producer(exchange_name,milkBytes,key3,"d");

        println!("WORKER: Sending to SHOP OWNER");
    }
}