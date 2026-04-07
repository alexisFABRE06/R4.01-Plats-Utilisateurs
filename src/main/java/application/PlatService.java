package application;

import domain.Plat;
import infrastructure.PlatInput;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des plats.
 * Gère les opérations CRUD sur les plats proposés par l'entreprise.
 */
@Dependent
public class PlatService {
    private final PlatRepositoryInterface platRepository;

    @Inject
    public PlatService(PlatRepositoryInterface platRepository) {
        this.platRepository = platRepository;
    }

    /**
     * Récupère tous les plats.
     */
    public List<Plat> listerTousLesPlats() {
        return platRepository.findAll();
    }

    /**
     * Récupère un plat par son ID.
     */
    public Optional<Plat> obtenirPlatParId(Long id) {
        return platRepository.findById(id);
    }

    /**
     * Crée un nouveau plat.
     */
    public Plat creerNouvellePlat(PlatInput input) {
        return platRepository.create(input);
    }

    /**
     * Modifie un plat existant.
     */
    public Optional<Plat> modifierPlat(Long id, PlatInput input) {
        return platRepository.update(id, input);
    }

    /**
     * Supprime un plat.
     */
    public boolean supprimerPlat(Long id) {
        return platRepository.delete(id);
    }
}
