package infrastructure;

import application.UtilisateurService;
import domain.Utilisateur;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Ressource REST pour la gestion des utilisateurs.
 * Expose les opérations CRUD sur les utilisateurs via HTTP.
 */
@Path("/utilisateurs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilisateurRessource {

    private final UtilisateurService service;

    @Inject
    public UtilisateurRessource(UtilisateurService service) {
        this.service = service;
    }

    /**
     * GET /utilisateurs - Récupère tous les utilisateurs
     */
    @GET
    public List<Utilisateur> getAll() {
        return service.listerTousLesUtilisateurs();
    }

    /**
     * GET /utilisateurs/{id} - Récupère un utilisateur par son ID
     */
    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Long id) {
        return service.obtenirUtilisateurParId(id)
                .map(utilisateur -> Response.ok(utilisateur).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Utilisateur non trouvé avec l'ID: " + id).build());
    }

    /**
     * POST /utilisateurs - Crée un nouvel utilisateur
     */
    @POST
    public Response create(UtilisateurInput input) {
        try {
            Utilisateur nouveau = service.creerNouvelUtilisateur(input);
            return Response.status(Response.Status.CREATED).entity(nouveau).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erreur lors de la création : " + e.getMessage()).build();
        }
    }

    /**
     * PUT /utilisateurs/{id} - Modifie un utilisateur existant
     */
    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, UtilisateurInput input) {
        return service.modifierUtilisateur(id, input)
                .map(utilisateur -> Response.ok(utilisateur).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Utilisateur non trouvé avec l'ID: " + id).build());
    }

    /**
     * DELETE /utilisateurs/{id} - Supprime un utilisateur
     */
    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        if (service.supprimerUtilisateur(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilisateur non trouvé avec l'ID: " + id).build();
        }
    }
}
