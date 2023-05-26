package org.acme;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/developer")
public class DeveloperResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Developer> developers(){
        return Developer.listAll();
    }

   @Transactional
    @GET
    @Path("new/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Developer newDeveloper(@PathParam("name") String name){
        Developer dev=new Developer();
        dev.name=name;
        dev.persist();
        return dev;

    }


}
