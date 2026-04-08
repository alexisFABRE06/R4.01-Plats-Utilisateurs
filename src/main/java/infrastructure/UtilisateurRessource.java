package infrastructure;

import application.UtilisateurRepositoryInterface;
import application.UtilisateurService;
import domain.Utilisateur;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Ressource REST pour la gestion des utilisateurs.
 * Expose les opérations CRUD sur les utilisateurs via HTTP.
 */
@Path("/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UtilisateurRessource {

    private UtilisateurService service;

    public UtilisateurRessource() {
    }

    public @Inject UtilisateurRessource(UtilisateurRepositoryInterface utilisateurRepo) {
        this.service = new UtilisateurService(utilisateurRepo);
    }

    @GET
    public String getAll() {
        return service.getAllUtilisateursJSON();
    }

    @GET
    @Path("{id}")
    public String getById(@PathParam("id") Long id) {
        String result = service.getUtilisateurJSON(id);
        if (result == null) {
            throw new NotFoundException();
        }
        return result;
    }

    @POST
    public Response create(Utilisateur utilisateur) {
        String created = service.createUtilisateurJSON(utilisateur);
        if (created == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, Utilisateur utilisateur) {
        if (!service.updateUtilisateur(id, utilisateur)) {
            throw new NotFoundException();
        }
        return Response.ok("updated").build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        if (!service.deleteUtilisateur(id)) {
            throw new NotFoundException();
        }
        return Response.ok("deleted").build();
    }
}
