package wonderland.rsocket.publish_subscribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.function.context.config.RoutingFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Function;

@SpringBootApplication
public class RSocketPublisher {

    public static void main(String[] args) {
        SpringApplication.run(RSocketPublisher.class, args);
    }

    @Autowired
    EventPublisher<String> eventPublisher;

//    Mono<RSocketRequester> rSocketRequester = RSocketRequester.builder()
//            .connectTcp("localhost", 7557);

    @EventListener
    public void handleContextRefreshEvent(ApplicationStartedEvent startedEvent) throws InterruptedException {
        Flux.interval(Duration.ofSeconds(3))
                .map(String::valueOf)
                .subscribe(aLong -> eventPublisher.publish(aLong));

//        rSocketRequester
//                .flatMapMany(requester -> requester
//                        .route(RoutingFunction.FUNCTION_NAME)
//                        .metadata("{\"func\":\"events\"}", MimeTypeUtils.APPLICATION_JSON)
//                        .data("mahdi")
//                        .retrieveFlux(String.class))
//                .subscribe(System.out::println);
//
//        Thread.sleep(5000);
//        rSocketRequester
//                .flatMapMany(requester -> requester
//                        .route(RoutingFunction.FUNCTION_NAME)
//                        .metadata("{\"func\":\"events\"}", MimeTypeUtils.APPLICATION_JSON)
//                        .data("aboli")
//                        .retrieveFlux(String.class))
//                .subscribe(System.out::println);
//
//
//        rSocketRequester
//                .flatMap(requester -> requester
//                        .route(RoutingFunction.FUNCTION_NAME)
//                        .metadata("{\"func\":\"echo\"}", MimeTypeUtils.APPLICATION_JSON)
//                        .data("mahdiiiiiiiiii")
//                        .retrieveMono(String.class))
//                .subscribe(System.out::println);
    }

    @Bean
    public Function<String, String> echo(){
        return Function.identity();
    }

    @Bean
    public Function<String, Flux<String>> events() {
        return name -> eventPublisher.map(name::concat);
    }

}
