package infrastructure;

import application.PlatRepositoryInterface;
import application.UtilisateurRepositoryInterface;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration de l'application JAX-RS.
 * Configure le point d'accès racine pour toutes les ressources REST.
 */
@ApplicationPath("/api")
@ApplicationScoped
public class RestApplication extends Application {
    private static final String ENV_DB_URL = "ARCHLOG_DB_URL";
    private static final String ENV_DB_USER = "ARCHLOG_DB_USER";
    private static final String ENV_DB_PASSWORD = "ARCHLOG_DB_PASSWORD";
    private static final Map<String, String> DOTENV = loadDotEnv();

    @Produces
    @ApplicationScoped
    public PlatRepositoryInterface openPlatRepository() {
        try {
            return new PlatRepositoryMariadb(
                    getRequiredEnv(ENV_DB_URL),
                    getRequiredEnv(ENV_DB_USER),
                    getRequiredEnv(ENV_DB_PASSWORD)
            );
        } catch (Exception e) {
            throw new RuntimeException("Impossible de creer le repository plats", e);
        }
    }

    @Produces
    @ApplicationScoped
    public UtilisateurRepositoryInterface openUtilisateurRepository() {
        try {
            return new UtilisateurRepositoryMariadb(
                    getRequiredEnv(ENV_DB_URL),
                    getRequiredEnv(ENV_DB_USER),
                    getRequiredEnv(ENV_DB_PASSWORD)
            );
        } catch (Exception e) {
            throw new RuntimeException("Impossible de creer le repository utilisateurs", e);
        }
    }

    public void closePlatRepository(@Disposes PlatRepositoryInterface platRepository) {
        platRepository.close();
    }

    public void closeUtilisateurRepository(@Disposes UtilisateurRepositoryInterface utilisateurRepository) {
        utilisateurRepository.close();
    }

    private String getRequiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = DOTENV.get(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Variable d'environnement manquante: " + key);
        }
        return value;
    }

    private static Map<String, String> loadDotEnv() {
        Map<String, String> values = new HashMap<>();

        try (InputStream in = RestApplication.class.getResourceAsStream("/.env")) {
            if (in != null) {
                readDotEnv(values, in);
                return values;
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier .env depuis le classpath", e);
        }

        Path localDotEnv = Path.of(".env");
        if (Files.exists(localDotEnv)) {
            try (InputStream in = Files.newInputStream(localDotEnv)) {
                readDotEnv(values, in);
            } catch (IOException e) {
                throw new RuntimeException("Impossible de lire le fichier .env local", e);
            }
        }

        return values;
    }

    private static void readDotEnv(Map<String, String> values, InputStream in) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int separator = line.indexOf('=');
                if (separator <= 0) {
                    continue;
                }
                String key = line.substring(0, separator).trim();
                String value = line.substring(separator + 1).trim();
                if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                values.put(key, value);
            }
        }
    }
}