package application;

import domain.Utilisateur;
import infrastructure.UtilisateurInput;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des utilisateurs.
 * Gère les opérations CRUD sur les utilisateurs (abonnés) de l'application.
 */
@Dependent
public class UtilisateurService {
    private final UtilisateurRepositoryInterface utilisateurRepository;

    @Inject
    public UtilisateurService(UtilisateurRepositoryInterface utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Récupère tous les utilisateurs.
     */
    public List<Utilisateur> listerTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public Optional<Utilisateur> obtenirUtilisateurParId(Long id) {
        return utilisateurRepository.findById(id);
    }

    /**
     * Crée un nouvel utilisateur.
     */
    public Utilisateur creerNouvelUtilisateur(UtilisateurInput input) {
        return utilisateurRepository.create(input);
    }

    /**
     * Modifie un utilisateur existant.
     */
    public Optional<Utilisateur> modifierUtilisateur(Long id, UtilisateurInput input) {
        return utilisateurRepository.update(id, input);
    }

    /**
     * Supprime un utilisateur.
     */
    public boolean supprimerUtilisateur(Long id) {
        return utilisateurRepository.delete(id);
    }
}
