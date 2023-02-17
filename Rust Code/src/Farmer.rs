use scheduled_thread_pool::ScheduledThreadPool;
use std::time::Duration;
use crate::{Amiquip, Soybean};

pub struct farmer;

impl farmer{

    pub fn run(&self){
        let exchange_name = "Soybean Topic";
        println!("FARMER: Collecting Soybeans...");

        let mut object = Soybean{name: String::from("soybean")};
        let bytes = bincode::serialize(&object).unwrap();

        std::thread::sleep(Duration::from_secs(2));

        println!("FARMER: Sending Soybeans to the Worker...");

        Amiquip::producer(exchange_name,bytes,"","f");

    }

}