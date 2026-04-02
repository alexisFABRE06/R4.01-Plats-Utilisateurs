package application;

import domain.Plat;
import infrastructure.PlatInput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service métier pour la gestion des plats.
 * Gère les opérations CRUD sur les plats proposés par l'entreprise.
 */
public class PlatService {
    // Simulation de la base de données (isolée dans le service)
    private static List<Plat> database = new ArrayList<>();
    private static AtomicLong idGenerator = new AtomicLong(1);

    static {
        // Initialisation avec les 8 plats du TP
        database.add(createPlat("Salade niçoise", "Laitue, tomates, œufs, anchois, olives", 9.50));
        database.add(createPlat("Aïoli", "Morue, œufs durs, légumes vapeur, sauce aïoli", 12.00));
        database.add(createPlat("Gratin dauphinois", "Pommes de terre, crème, fromage, noix de muscade", 8.75));
        database.add(createPlat("Ratatouille", "Aubergine, courgette, tomate, poivron, herbes", 7.50));
        database.add(createPlat("Côte de veau rôtie", "Viande tendre rôtie aux herbes provençales", 15.50));
        database.add(createPlat("Bouillabaisse", "Poisson de roche, safran, croûtons, rouille", 14.00));
        database.add(createPlat("Tapenade", "Olives noires, câpres, anchois, citron", 6.50));
        database.add(createPlat("Poutine", "Frites, sauce brune, fromage en grains", 8.00));
    }

    private static Plat createPlat(String nom, String description, Double prix) {
        Plat p = new Plat(nom, description, prix);
        p.setId(idGenerator.getAndIncrement());
        return p;
    }

    /**
     * Récupère tous les plats.
     */
    public List<Plat> listerTousLesPlats() {
        return new ArrayList<>(database);
    }

    /**
     * Récupère un plat par son ID.
     */
    public Optional<Plat> obtenirPlatParId(Long id) {
        return database.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    /**
     * Crée un nouveau plat.
     */
    public Plat creerNouvellePlat(PlatInput input) {
        Plat plat = new Plat();
        plat.setId(idGenerator.getAndIncrement());
        plat.setNom(input.nom);
        plat.setDescription(input.description);
        plat.setPrix(input.prix);
        plat.setDateCreation(LocalDateTime.now());
        database.add(plat);
        return plat;
    }

    /**
     * Modifie un plat existant.
     */
    public Optional<Plat> modifierPlat(Long id, PlatInput input) {
        var optPlat = database.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (optPlat.isPresent()) {
            Plat plat = optPlat.get();
            plat.setNom(input.nom);
            plat.setDescription(input.description);
            plat.setPrix(input.prix);
            plat.setDateModification(LocalDateTime.now());
        }
        return optPlat;
    }

    /**
     * Supprime un plat.
     */
    public boolean supprimerPlat(Long id) {
        return database.removeIf(p -> p.getId().equals(id));
    }
}
