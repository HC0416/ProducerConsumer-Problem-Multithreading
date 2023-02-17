extern crate arraylist;

use crate::{Amiquip, Flour, Milk, Residue};
use std::cell::RefCell;
use std::fs::copy;
use std::time::Duration;

use arraylist::arl::ArrayList;
use arraylist::arraylist;

pub struct shopOwner;

/*pub struct flourRack{
    vec: RefCell<Vec<Flour>>,
}*/


pub static mut product_Type: i32 = 0;

impl shopOwner{

    pub fn run(&self, mut bindingKey:String, mut customerID:i32){

        let key_FnR = "FLOUR.RESIDUE";
        let key_FnM = "MILK.FLOUR";
        let key_RnM = "RESIDUE.MILK";
        let key_FnRnM = "FLOUR.RESIDUE.MILK";

        let exchange_name = "Rabbit MQ Customer Topic";

        let mut flour = Flour{name: String::from("flour")};
        let mut residue = Residue{name: String::from("residue")};
        let mut milk = Milk{name: String::from("milk")};

        let FlourBytes = bincode::serialize(&flour).unwrap();
        let ResidueBytes = bincode::serialize(&residue).unwrap();
        let milkBytes = bincode::serialize(&milk).unwrap();

        //std::thread::sleep(Duration::from_secs(1));
        //println!("SHOP OWNER: Serving Customer {} ...", customerID);
        if(bindingKey == "*.FLOUR"){
            unsafe { product_Type = 1; }
            Amiquip::producer(exchange_name,FlourBytes,key_FnM,"t");
            //println!("SHOP OWNER: Serving Customer {}...", customerID);
            unsafe { product_Type = 2; }
            Amiquip::producer(exchange_name,milkBytes,key_FnM,"t");
        }
        else if (bindingKey == "FLOUR.*") {
            unsafe { product_Type = 1; }
            Amiquip::producer(exchange_name,FlourBytes,key_FnR,"t");
            unsafe { product_Type = 3; }
            Amiquip::producer(exchange_name,ResidueBytes,key_FnR,"t");
        }
        else if (bindingKey == "RESIDUE.*") {
            unsafe { product_Type = 3; }
            Amiquip::producer(exchange_name,ResidueBytes,key_RnM,"t");
            unsafe { product_Type = 2; }
            Amiquip::producer(exchange_name,milkBytes,key_RnM,"t");
        }
        else {
            unsafe { product_Type = 1; }
            Amiquip::producer(exchange_name,FlourBytes,key_FnRnM,"t");
            unsafe { product_Type = 3; }
            Amiquip::producer(exchange_name,ResidueBytes,key_FnRnM,"t");
            unsafe { product_Type = 2; }
            Amiquip::producer(exchange_name,milkBytes,key_FnRnM,"t");
        }



    }

    pub fn collect(&self){

        let exchange_name = "Soybean Direct";

        let key1 = "Flour key";
        let key2 = "Residue Key";
        let key3 = "Milk key";

        Amiquip::consumer(exchange_name,key1,"d");
        Amiquip::consumer(exchange_name,key2,"d");
        Amiquip::consumer(exchange_name,key3,"d");
    }



    pub fn collectFlour(mut flour:Flour){
        println!("SHOP OWNER: {} is received!", flour.to_string());
    }

    pub fn collectResidue(mut residue:Residue){
        println!("SHOP OWNER: {} is received!", residue.to_string());
    }

    pub fn collectMilk(mut milk:Milk){

        println!("SHOP OWNER: {} is received!", milk.to_string());
    }
}

/*impl flourRack {
    pub fn created()->flourRack {
        flourRack {vec: RefCell::new(Vec::new()) }
    }
    pub fn add(&self, value: Flour){
        self.vec.borrow_mut().push(value);
        println!("{:?}", self.vec);
    }
}*/


