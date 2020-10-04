# Publish-Subscribe using RSocket protocol

thanks to @Olegz and @spencergibb
https://www.youtube.com/watch?v=XWgXwpQ3dU4&t=1697s

### RSocket
 is a bi-directional message based binary application level protocol.
 It can be transported using tcp, websocket and ... .
 It is designed to be reactive freindly by nature.
 For example it support back pressure very nicely.
 However, in a distributed system, usgin RSocket does not bring decoupleing in the 
 way that, for example, kafka brings. Because services need to know each others address, port, and ... .
 
 ### RSocket-Broker
 can be used as a middle wear which has a high availability (clustering) that all the RSocket parties,
 connect to. Then what ever request they have, they send only need to know a couple of names to get the results through the 
 broker which has an ongoing connection to all the parties.
 
 ##Publish-Subscribe
 Infinite number of publishers can connect to RSocket-Broker and expose "APIs" (Streams ready to be subscribed).
 They can be different instances of same application. Infinite number of subscribers can also connect to the RSocker-Broker (doesn't need to be intances of same application).
 Then clients need to know the name of application they want to get data from (in the simplest form) and name of a function (which is eqiuvalent to name of a topic).
 Frm there on, the client uses that two names and put the load balancing and service registry on the shoulders of broker to get an stream of data from publisher.
 
 ``` rSocketRequester
                            .route("events")
                            .metadata(metadata.address("rsocket-publisher"))
                            .data("someThing")
                            .retrieveFlux(String.class)
                    .subscribe(System.out::println);
```


