package infrastructure;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuration de l'application JAX-RS.
 * Configure le point d'accès racine pour toutes les ressources REST.
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // Les ressources seront détectées automatiquement
}