# backend_engineering_assignment
backend engineering assignment

1) to start application open terminal in project location and type command 
	mvn spring-boot:run

2) this should be a single endpoint aggregating information from few other microservices, I decided to mock external services inside application. To maintain situation that service won't respond I decided to use TimeLimiter 
from resilence4j. Other options I was considering were create asynchronous rest call, which can get data partially (but it requires multiple calls to the same endpoint), using separate threads for every part of 
data (this could complicate code too much) and reactive approach (unfortunatelly I don't know enough about reactive libraries to use one of them, and there was no time to learn about them). I used lombok to reduce boilerpate code,
and mockito for preparing unit tests.

3) With more time I would check reactive approach, because I think it would be useful here, data would load when they're ready and it wouldn't stop us from delivering previous data earlier. Markets requires different approach, 
current approach was chosen olny because of use of limited mock services. I would also prepare better solution 
for customne information, I'm not fond of how it looks like now. I would prepare more unit tests. How data from different markets are collected also requires further analysis.
CustomerId shouldn't be passed by plain text, at least some encoding should be added.

4) Design question:
Option A: "The Assortment team wants to add a 'Related Products' service (200ms latency,
90% reliability). How would your design accommodate this? Should it be required or
optional?"

It would require to create new service (with interface) and add it to RestController. call to this service should be prepared similar to other calls, like in "obtain..." methods. It should be optional, it's not required by 
customer to make a purchase.
