package wonderland.rsocket.publish_subscribe;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import reactor.extra.processor.TopicProcessor;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class EventPublisher<T> {

    final private FluxProcessor processor;
    final private FluxSink<T> sink;

    public EventPublisher() {
        //its not a stateful topic. If you are late, you will miss what has already published
        this.processor = TopicProcessor.create().serialize(); //topicProcessor publishes to all subscribers.
        //reactor.extra.processor.WorkQueueProcessor publishes load balances between subscribers (like partitioning in kafka)
        this.sink = processor.sink();
    }

    public void publish(T event){
        sink.next(event);
    }

    public void subscribe(Consumer<T> consumer){
        processor.subscribe(consumer);
    }

    public Flux<T> map(Function<T, T> map){
        return processor.map(map);
    }
}
