package application;

import domain.Plat;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.List;

/**
 * Service métier pour la gestion des plats.
 * Gère les opérations CRUD sur les plats proposés par l'entreprise.
 */
public class PlatService {
    private final PlatRepositoryInterface platRepository;

    public PlatService(PlatRepositoryInterface platRepository) {
        this.platRepository = platRepository;
    }

    public String getAllPlatsJSON() {
        List<Plat> allPlats = platRepository.getAllPlats();
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(allPlats);
        } catch (Exception e) {
            return "[]";
        }
    }

    public String getPlatJSON(Long id) {
        Plat plat = platRepository.getPlat(id);
        if (plat == null) {
            return null;
        }
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(plat);
        } catch (Exception e) {
            return null;
        }
    }

    public String createPlatJSON(Plat plat) {
        Plat created = platRepository.createPlat(plat);
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.toJson(created);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updatePlat(Long id, Plat plat) {
        return platRepository.updatePlat(id, plat);
    }

    public boolean deletePlat(Long id) {
        return platRepository.delete(id);
    }
}
