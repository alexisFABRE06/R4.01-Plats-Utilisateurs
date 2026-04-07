package infrastructure;

import application.PlatRepositoryInterface;
import application.UtilisateurRepositoryInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuration de l'application JAX-RS.
 * Configure le point d'accès racine pour toutes les ressources REST.
 */
@ApplicationPath("/api")
@ApplicationScoped
public class RestApplication extends Application {
    private static final String DEFAULT_DB_URL =
            "jdbc:mariadb://mysql-alexs.alwaysdata.net/alexs_archlog_bdd";
    private static final String DEFAULT_DB_USER = "alexs_archlog";
    private static final String DEFAULT_DB_PASSWORD = "AlexisArchlog2406!";

    @Produces
    @ApplicationScoped
    public Connection openDbConnection() {
        String dbUrl = System.getenv().getOrDefault("ARCHLOG_DB_URL", DEFAULT_DB_URL);
        String dbUser = System.getenv().getOrDefault("ARCHLOG_DB_USER", DEFAULT_DB_USER);
        String dbPassword = System.getenv().getOrDefault("ARCHLOG_DB_PASSWORD", DEFAULT_DB_PASSWORD);

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (Exception e) {
            throw new RuntimeException("Impossible d'ouvrir la connexion MariaDB", e);
        }
    }

    @Produces
    @ApplicationScoped
    public PlatRepositoryInterface createPlatRepository(Connection dbConnection) {
        return new PlatRepositoryMariadb(dbConnection);
    }

    @Produces
    @ApplicationScoped
    public UtilisateurRepositoryInterface createUtilisateurRepository(Connection dbConnection) {
        return new UtilisateurRepositoryMariadb(dbConnection);
    }

    public void closeDbConnection(@Disposes Connection dbConnection) {
        try {
            dbConnection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la fermeture de la connexion MariaDB", e);
        }
    }
}