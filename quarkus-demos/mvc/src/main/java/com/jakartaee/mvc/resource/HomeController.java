package com.jakartaee.mvc.resource;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Controlador para la página de inicio.
 */
@Path("/")
public class HomeController {
    
    @Inject
    @Location("index.html")
    Template index;
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance home() {
        return index.instance();
    }
}

