package application;

import domain.Utilisateur;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.List;

/**
 * Service métier pour la gestion des utilisateurs.
 * Gère les opérations CRUD sur les utilisateurs (abonnés) de l'application.
 */
public class UtilisateurService {
    private final UtilisateurRepositoryInterface utilisateurRepository;

    public UtilisateurService(UtilisateurRepositoryInterface utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public String getAllUtilisateursJSON() {
        List<Utilisateur> allUtilisateurs = utilisateurRepository.getAllUtilisateurs();
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(allUtilisateurs);
        } catch (Exception e) {
            return "[]";
        }
    }

    public String getUtilisateurJSON(Long id) {
        Utilisateur utilisateur = utilisateurRepository.getUtilisateur(id);
        if (utilisateur == null) {
            return null;
        }
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(utilisateur);
        } catch (Exception e) {
            return null;
        }
    }

    public String createUtilisateurJSON(Utilisateur utilisateur) {
        Utilisateur created = utilisateurRepository.createUtilisateur(utilisateur);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(created);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updateUtilisateur(Long id, Utilisateur utilisateur) {
        return utilisateurRepository.updateUtilisateur(id, utilisateur);
    }

    public boolean deleteUtilisateur(Long id) {
        return utilisateurRepository.delete(id);
    }
}
