package org.acme;

import io.reactivex.Flowable;
import jakarta.enterprise.context.ApplicationScoped;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class StreamBean {      //we want to make this class available for injection: therefore: CDI annotation: @ApplicationScoped. Additionally, it will be one instance for the whole application.

    AtomicInteger counter = new AtomicInteger();
    public Publisher<String> stream (){

        return Flowable.interval(500, TimeUnit.MILLISECONDS)  //every 500 milliseconds we will return a value
                .map(i->counter.incrementAndGet())
                .map(i-> i.toString());

    }
}
