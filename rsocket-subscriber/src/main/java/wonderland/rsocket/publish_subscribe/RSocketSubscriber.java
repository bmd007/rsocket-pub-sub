package wonderland.rsocket.publish_subscribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.function.context.config.RoutingFunction;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RSocketSubscriber {

    public static void main(String[] args) {
        SpringApplication.run(RSocketSubscriber.class, args);
    }

    Mono<RSocketRequester> rSocketRequester = RSocketRequester.builder()
            .connectTcp("localhost", 7557);

    @EventListener
    public void handleContextRefreshEvent(ApplicationStartedEvent startedEvent) throws InterruptedException {
        rSocketRequester
                .flatMapMany(requester -> requester
                        .route(RoutingFunction.FUNCTION_NAME)
                        .metadata("{\"func\":\"events\"}", MimeTypeUtils.APPLICATION_JSON)
                        .data("mahdi")
                        .retrieveFlux(String.class))
                .subscribe(System.out::println);

        Thread.sleep(5000);

        rSocketRequester
                .flatMapMany(requester -> requester
                        .route(RoutingFunction.FUNCTION_NAME)
                        .metadata("{\"func\":\"events\"}", MimeTypeUtils.APPLICATION_JSON)
                        .data("aboli")
                        .retrieveFlux(String.class))
                .subscribe(System.out::println);


        rSocketRequester
                .flatMap(requester -> requester
                        .route(RoutingFunction.FUNCTION_NAME)
                        .metadata("{\"func\":\"echo\"}", MimeTypeUtils.APPLICATION_JSON)
                        .data("mahdiiiiiiiiii")
                        .retrieveMono(String.class))
                .subscribe(System.out::println);
    }


}
