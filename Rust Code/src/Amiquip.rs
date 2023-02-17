use amiquip::{Connection, ConsumerMessage, ConsumerOptions, QueueDeclareOptions, Exchange, Publish, Result, ExchangeType, ExchangeDeclareOptions, FieldTable};
use bincode::deserialize;
use crate::Worker::worker;
use crate::ShopOwner::shopOwner;
use crate::Soybean;
use crate::Flour;
use crate::Residue;
use crate::Milk;
use crate::Customer::{Customer_E, Customer_FnM};
use crate::Customer::Customer_FnR;
use crate::Customer::Customer_RnM;
use crate::ShopOwner::product_Type;
use std::sync::mpsc::*;


pub fn producer(mut exchange:&str, mut object:Vec<u8>, mut key:&str, mut exchange_type:&str)-> Result<()>{

    let mut connection = Connection::insecure_open("amqp://guest:guest@localhost:5672")?;

    let channel = connection.open_channel(None)?;

    if(exchange_type == "f"){
        channel.exchange_declare(ExchangeType::Fanout, exchange, ExchangeDeclareOptions::default());
    }
    else if(exchange_type == "d"){
        channel.exchange_declare(ExchangeType::Direct, exchange, ExchangeDeclareOptions::default());
    }
    else{
        channel.exchange_declare(ExchangeType::Topic, exchange, ExchangeDeclareOptions::default());
    }

    channel.basic_publish(exchange,Publish::new(&object,key));

    connection.close()
}

pub fn consumer(mut exchange:&str, mut key:&str, mut exchange_type:&str)-> Result<()>{

    let (sender, receiver): (Sender<Flour>, Receiver<Flour>) = channel();
    let (sender2, receiver2): (Sender<Residue>, Receiver<Residue>) = channel();

    let mut connection = Connection::insecure_open("amqp://guest:guest@localhost:5672")?;

    let channel = connection.open_channel(None)?;

    if(exchange_type == "f"){
        channel.exchange_declare(ExchangeType::Fanout,exchange,ExchangeDeclareOptions::default());
    }
    else if(exchange_type == "d"){
        channel.exchange_declare(ExchangeType::Direct,exchange,ExchangeDeclareOptions::default());
    }
    else{
        channel.exchange_declare(ExchangeType::Topic,exchange,ExchangeDeclareOptions::default());
    }

    let queue = channel.queue_declare(exchange, QueueDeclareOptions::default())?;

    channel.queue_bind(exchange,exchange,key,FieldTable::new());

    let consumer = queue.consume(ConsumerOptions::default())?;

    for (i, message) in consumer.receiver().iter().enumerate() {
        match message {
            ConsumerMessage::Delivery(delivery) => {

                if(exchange == "Soybean Topic"){
                    let mut soybean = Soybean{name: deserialize(&delivery.body).unwrap()};
                    worker::collect(soybean);
                }

                if(exchange == "Soybean Direct" && key == "Milk key") {
                    let mut milk = Milk{name: deserialize(&delivery.body).unwrap()};
                    shopOwner::collectMilk(milk);
                }
                else if(exchange == "Soybean Direct" && key == "Flour key") {
                    let mut flour = Flour { name: deserialize(&delivery.body).unwrap() };
                    shopOwner::collectFlour(flour);
                }
                else if(exchange == "Soybean Direct" && key == "Residue Key") {
                    let mut residue = Residue{name: deserialize(&delivery.body).unwrap()};
                    shopOwner::collectResidue(residue);
                }


                else if(exchange == "Rabbit MQ Customer Topic" && key == "*.FLOUR"){
                    //let mut flour = Flour { name: deserialize(&delivery.body).unwrap() };
                    //Customer_FnM::purchasesss(flour);

                    unsafe {
                        if(product_Type == 1){
                            let mut flour = Flour { name: deserialize(&delivery.body).unwrap() };
                            sender.send(flour).unwrap();
                        }
                        else{
                            let mut milk = Milk { name: deserialize(&delivery.body).unwrap() };

                            let mut f = receiver.recv().unwrap();
                            Customer_FnM::purchaseAll(f, milk);
                        }
                    }
                }

                else if(exchange == "Rabbit MQ Customer Topic" && key == "FLOUR.*"){

                    unsafe {
                        if(product_Type == 1){
                            let mut flour = Flour { name: deserialize(&delivery.body).unwrap() };
                            sender.send(flour).unwrap();
                        }
                        else{
                            let mut residue = Residue { name: deserialize(&delivery.body).unwrap() };

                            let mut f = receiver.recv().unwrap();
                            Customer_FnR::purchaseAll(f, residue);
                        }
                    }
                }

                else if(exchange == "Rabbit MQ Customer Topic" && key == "RESIDUE.*"){
                    unsafe {
                        if (product_Type == 3) {
                            let mut residue = Residue { name: deserialize(&delivery.body).unwrap() };
                            sender2.send(residue).unwrap();
                        }
                        else{
                            let mut milk = Milk { name: deserialize(&delivery.body).unwrap() };

                            let mut r = receiver2.recv().unwrap();
                            Customer_RnM::purchaseAll(r, milk);
                        }
                    }
                }
                 /*
                else if(exchange == "Rabbit MQ Customer Topic" && key == "*.*.*"){
                    unsafe {
                        if (product_Type == 1) {
                            let mut flour = Flour { name: deserialize(&delivery.body).unwrap() };
                            sender.send(flour).unwrap();
                        }
                        else if (product_Type == 3) {
                            let mut residue = Residue { name: deserialize(&delivery.body).unwrap() };
                            sender2.send(residue).unwrap();
                        }
                        else{
                            let mut milk = Milk { name: deserialize(&delivery.body).unwrap() };

                            let mut f = receiver.recv().unwrap();
                            let mut r = receiver2.recv().unwrap();
                            Customer_E::purchaseAll(f,r,milk);
                        }
                    }
                }*/



                consumer.ack(delivery)?;

            }
            other => {
                println!("ended: {:?}", other);
                break;
            }
        }
    }

    connection.close()

}

pub fn purge_queue() -> Result<()>{
    let mut connection = Connection::insecure_open("amqp://guest:guest@localhost:5672")?;

    let channel = connection.open_channel(None)?;
    channel.queue_purge("Rabbit MQ Customer Topic");
    channel.queue_purge("Soybean Direct");
    channel.queue_purge("Soybean Topic");

    connection.close()
}