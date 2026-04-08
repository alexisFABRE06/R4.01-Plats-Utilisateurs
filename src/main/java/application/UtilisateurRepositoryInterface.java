package application;

import domain.Utilisateur;
import java.util.ArrayList;

/**
 * Contrat d'acces aux donnees des utilisateurs.
 */
public interface UtilisateurRepositoryInterface {
    void close();

    Utilisateur getUtilisateur(Long id);

    ArrayList<Utilisateur> getAllUtilisateurs();

    Utilisateur createUtilisateur(Utilisateur utilisateur);

    boolean updateUtilisateur(Long id, Utilisateur utilisateur);

    boolean delete(Long id);
}

