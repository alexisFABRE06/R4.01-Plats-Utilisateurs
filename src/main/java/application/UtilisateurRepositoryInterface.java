package application;

import domain.Utilisateur;
import infrastructure.UtilisateurInput;

import java.util.List;
import java.util.Optional;

/**
 * Contrat d'acces aux donnees des utilisateurs.
 */
public interface UtilisateurRepositoryInterface {
    List<Utilisateur> findAll();

    Optional<Utilisateur> findById(Long id);

    Utilisateur create(UtilisateurInput input);

    Optional<Utilisateur> update(Long id, UtilisateurInput input);

    boolean delete(Long id);
}

