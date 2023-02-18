# ProducerConsumer-Problem-Multithreading

## The aim of this project is to compare the performance of two programming languages (Java & Rust) with implementing the concurrent concept. A simulation will be developed using two different programming languages which are Java and Rust in order to identify how the performance of the system will be affected by utilizing different programming languages to develop.


### Simulation Scenario

This simulation will be based on a Producer-Consumer problem where the producer will constantly create data onto the queue while the consumer will take the data 
from the queue. Both of the simulation will have similar requirements and outputs. As it can be observed from the figure, the Farmer (producer) will first produce the soybean for the Worker (consumer) to consume. After that, the Worker will start producing three types of products from the soybeans. In this case, the Worker will also act as a producer and the Shop Owner (consumer) will begin to consume all of the products that were manufactured by the Worker. The Shop Owner will then place those products on their respective racks. At this time, the Customer (consumer) will begin to enter the shop and will randomly purchase one or more items. The Shop Owner will be acting as a producer as well and sell the item to the customer based on their preference. The Customer will consume the item from the rack and then leave the shop. Benchmarks will be performed for analyzing the performance of the system for both of simulations and comparison will be made.

For Java implementation, RabbitMQ will be used as a messaging broker to transfer the message between producer and consumer. Java Microbenchmark Harness (JMH) will be 
used to benchmark the scenario. As for Rust implementation, Amiquip which is a client of RabbitMQ written in Rust will be used to transfer messages while the bma_benchmark will be used for benchmarking.

### Results
<img src="https://user-images.githubusercontent.com/82216057/219592380-2255619a-9b04-4b0f-ac1c-182db5ca7103.jpg" width="700" height="800">

From the table above, it can be observed that in terms of throughput, the bma_benchmark for Rust did not provide a much clearer detail of the score. However, in terms of executing time, Rust is slightly ahead of Java by two seconds. Thus, it can be concluded Rust performs better than Java in terms of speed. One of the main reasons would be Java is using garbage collection for managing memory.
