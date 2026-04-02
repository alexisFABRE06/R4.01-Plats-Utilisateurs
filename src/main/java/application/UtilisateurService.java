package application;

import domain.Utilisateur;
import infrastructure.UtilisateurInput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service métier pour la gestion des utilisateurs.
 * Gère les opérations CRUD sur les utilisateurs (abonnés) de l'application.
 */
public class UtilisateurService {
    // Simulation de la base de données (isolée dans le service)
    private static List<Utilisateur> database = new ArrayList<>();
    private static AtomicLong idGenerator = new AtomicLong(1);

    static {
        // Initialisation avec 4 utilisateurs du TP
        database.add(createUtilisateur("Dupont", "Marie", "marie.dupont@email.com", "0612345678", "123 Rue de la Paix, Marseille"));
        database.add(createUtilisateur("Martin", "Jean", "jean.martin@email.com", "0687654321", "456 Avenue des Fleurs, Aix-en-Provence"));
        database.add(createUtilisateur("Bernard", "Sophie", "sophie.bernard@email.com", "0698765432", "789 Boulevard de la Mer, Marseille"));
        database.add(createUtilisateur("Laurent", "Pierre", "pierre.laurent@email.com", "0654321289", "321 Chemin de la Montagne, Aix-en-Provence"));
    }

    private static Utilisateur createUtilisateur(String nom, String prenom, String email, String telephone, String adresse) {
        Utilisateur u = new Utilisateur(nom, prenom, email, telephone, adresse);
        u.setId(idGenerator.getAndIncrement());
        return u;
    }

    /**
     * Récupère tous les utilisateurs.
     */
    public List<Utilisateur> listerTousLesUtilisateurs() {
        return new ArrayList<>(database);
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public Optional<Utilisateur> obtenirUtilisateurParId(Long id) {
        return database.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    /**
     * Crée un nouvel utilisateur.
     */
    public Utilisateur creerNouvelUtilisateur(UtilisateurInput input) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(idGenerator.getAndIncrement());
        utilisateur.setNom(input.nom);
        utilisateur.setPrenom(input.prenom);
        utilisateur.setEmail(input.email);
        utilisateur.setTelephone(input.telephone);
        utilisateur.setAdresseDefaut(input.adresseDefaut);
        utilisateur.setDateInscription(LocalDateTime.now());
        database.add(utilisateur);
        return utilisateur;
    }

    /**
     * Modifie un utilisateur existant.
     */
    public Optional<Utilisateur> modifierUtilisateur(Long id, UtilisateurInput input) {
        var optUser = database.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (optUser.isPresent()) {
            Utilisateur utilisateur = optUser.get();
            utilisateur.setNom(input.nom);
            utilisateur.setPrenom(input.prenom);
            utilisateur.setEmail(input.email);
            utilisateur.setTelephone(input.telephone);
            utilisateur.setAdresseDefaut(input.adresseDefaut);
        }
        return optUser;
    }

    /**
     * Supprime un utilisateur.
     */
    public boolean supprimerUtilisateur(Long id) {
        return database.removeIf(u -> u.getId().equals(id));
    }
}
