package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

import java.util.concurrent.CompletionStage;

@Path("/hello")
public class GreetingResource {

    @Inject
    @ConfigProperty(name="greeting")
    String greeting;


    @Inject
    StreamBean bean;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return greeting;
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> helloStream(){

        return bean.stream();

    }




   //reactive programming
    @GET
    @Path("/async")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> helloAsync()
    {
        return ReactiveStreams.of("h","e","l","l","o")
                .map(s->s.toUpperCase())
                .distinct()      //not duplicate characters
                .toList().run().thenApply(l->l.toString());
    }

    @GET
    @Path("/person")
    @Produces(MediaType.APPLICATION_JSON)
    public Person helloPerson()
    {
        return new Person("John Doe",23);
    }


}
