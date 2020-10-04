package wonderland.rsocket.publish_subscribe;

import io.rsocket.routing.client.spring.RoutingMetadata;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.rsocket.RSocketRequester;


@SpringBootApplication
public class RSocketSubscriber {

    public static void main(String[] args) {
        SpringApplication.run(RSocketSubscriber.class, args);
    }

    private final RSocketRequester rSocketRequester;
    private final RoutingMetadata metadata;

    public RSocketSubscriber(RSocketRequester requester, RoutingMetadata metadata) {
        this.rSocketRequester = requester;
        this.metadata = metadata;
    }

    @EventListener
    public void onStart(ApplicationReadyEvent event) throws InterruptedException {
        rSocketRequester
                        .route("events")
                        .metadata(metadata.address("rsocket-publisher"))
                        .data("mahdi")
                        .retrieveFlux(String.class)
                .subscribe(System.out::println);

        Thread.sleep(5000);

        rSocketRequester.route("events")
                        .metadata(metadata.address("rsocket-publisher"))
                        .data("aboli")
                        .retrieveFlux(String.class)
                .subscribe(System.out::println);

        Thread.sleep(5000);

        rSocketRequester.route("echo")
                        .metadata(metadata.address("rsocket-publisher"))
                        .data("mahdiiiiiiiiii")
                        .retrieveMono(String.class)
                .subscribe(System.out::println);
    }


}
