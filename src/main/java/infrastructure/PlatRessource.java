package infrastructure;

import application.PlatRepositoryInterface;
import application.PlatService;
import domain.Plat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Ressource REST pour la gestion des plats.
 * Expose les opérations CRUD sur les plats via HTTP.
 */
@Path("/plats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PlatRessource {

    private PlatService service;

    public PlatRessource() {
    }

    public @Inject PlatRessource(PlatRepositoryInterface platRepo) {
        this.service = new PlatService(platRepo);
    }

    /**
     * GET /plats - Récupère tous les plats
     */
    @GET
    public String getAll() {
        return service.getAllPlatsJSON();
    }

    /**
     * GET /plats/{id} - Récupère un plat par son ID
     */
    @GET
    @Path("{id}")
    public String getById(@PathParam("id") Long id) {
        String result = service.getPlatJSON(id);
        if (result == null) {
            throw new NotFoundException();
        }
        return result;
    }

    /**
     * POST /plats - Crée un nouveau plat
     */
    @POST
    public Response create(Plat plat) {
        String created = service.createPlatJSON(plat);
        if (created == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * PUT /plats/{id} - Modifie un plat existant
     */
    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, Plat plat) {
        if (!service.updatePlat(id, plat)) {
            throw new NotFoundException();
        }
        return Response.ok("updated").build();
    }

    /**
     * DELETE /plats/{id} - Supprime un plat
     */
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!service.deletePlat(id)) {
            throw new NotFoundException();
        }
        return Response.ok("deleted").build();
    }
}
