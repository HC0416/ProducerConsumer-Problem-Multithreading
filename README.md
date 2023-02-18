# ProducerConsumer-Problem-Multithreading

## The aim of this project is to compare the performance of two programming languages (Java & Rust) with implementing the concurrent concept. A simulation will be developed using two different programming languages which are Java and Rust in order to identify how the performance of the system will be affected by utilizing different programming languages to develop.


### Simulation Scenario
<img src="https://user-images.githubusercontent.com/82216057/219869520-e083d10a-c940-4ac0-8ec3-d2e100756ee7.png" width="300" height="500">

This simulation will be based on a Producer-Consumer problem where the producer will constantly create data onto the queue while the consumer will take the data 
from the queue. Both of the simulation will have similar requirements and outputs. As it can be observed from the figure, the Farmer (producer) will first produce the soybean for the Worker (consumer) to consume. After that, the Worker will start producing three types of products from the soybeans. In this case, the Worker will also act as a producer and the Shop Owner (consumer) will begin to consume all of the products that were manufactured by the Worker. The Shop Owner will then place those products on their respective racks. At this time, the Customer (consumer) will begin to enter the shop and will randomly purchase one or more items. The Shop Owner will be acting as a producer as well and sell the item to the customer based on their preference. The Customer will consume the item from the rack and then leave the shop. Benchmarks will be performed for analyzing the performance of the system for both of simulations and comparison will be made.

For Java implementation, RabbitMQ will be used as a messaging broker to transfer the message between producer and consumer. Java Microbenchmark Harness (JMH) will be 
used to benchmark the scenario. As for Rust implementation, Amiquip which is a client of RabbitMQ written in Rust will be used to transfer messages while the bma_benchmark will be used for benchmarking.

### Results
<img src="https://user-images.githubusercontent.com/82216057/219869757-12339cbc-85b2-48b9-b914-b730ca15ec5c.png" width="600" height="300">

From the table above, it can be observed that in terms of throughput, the bma_benchmark for Rust did not provide a much clearer detail of the score. However, in terms of executing time, Rust is slightly ahead of Java by two seconds. Thus, it can be concluded Rust performs better than Java in terms of speed. One of the main reasons would be Java is using garbage collection for managing memory.

### Conclusion
In summary, this research has found that the performance of real-time systems can be influenced by programming languages. Additionally, this study also showed that 
performance testing is essential for the development of any real-time system. Therefore, for the development of real-time systems, it is crucial to select the appropriate programming language and performance testing techniques because different languages and techniques may have different compilation runtime behavior, features, and performance outcomes. In this research, it can be concluded as developing the simulation using Java has a slightly better performance than using Rust. This is mostly caused by the fact that Java utilizes garbage collection to manage memory, which lowers performance. However, a lot of benchmarking is required to run and keep track of the system's throughput, memory allocation, and numerous other aspects to make sure the RTS performance is at the best rate. As a result, additional study and development could be done to enhance the functionality of a Real-Time system by looking at other area
