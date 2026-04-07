package infrastructure;

import application.PlatService;
import domain.Plat;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Ressource REST pour la gestion des plats.
 * Expose les opérations CRUD sur les plats via HTTP.
 */
@Path("/plats")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlatRessource {

    private final PlatService service;

    @Inject
    public PlatRessource(PlatService service) {
        this.service = service;
    }

    /**
     * GET /plats - Récupère tous les plats
     */
    @GET
    public List<Plat> getAll() {
        return service.listerTousLesPlats();
    }

    /**
     * GET /plats/{id} - Récupère un plat par son ID
     */
    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        return service.obtenirPlatParId(id)
                .map(plat -> Response.ok(plat).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Plat non trouvé avec l'ID: " + id).build());
    }

    /**
     * POST /plats - Crée un nouveau plat
     */
    @POST
    public Response create(PlatInput input) {
        try {
            Plat nouveau = service.creerNouvellePlat(input);
            return Response.status(Response.Status.CREATED).entity(nouveau).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erreur lors de la création : " + e.getMessage()).build();
        }
    }

    /**
     * PUT /plats/{id} - Modifie un plat existant
     */
    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, PlatInput input) {
        return service.modifierPlat(id, input)
                .map(plat -> Response.ok(plat).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Plat non trouvé avec l'ID: " + id).build());
    }

    /**
     * DELETE /plats/{id} - Supprime un plat
     */
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        if (service.supprimerPlat(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Plat non trouvé avec l'ID: " + id).build();
        }
    }
}
