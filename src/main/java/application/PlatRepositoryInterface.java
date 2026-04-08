package application;

import domain.Plat;
import java.util.ArrayList;

/**
 * Contrat d'accès aux données des plats.
 */
public interface PlatRepositoryInterface {
    void close();

    Plat getPlat(Long id);

    ArrayList<Plat> getAllPlats();

    Plat createPlat(Plat plat);

    boolean updatePlat(Long id, Plat plat);

    boolean delete(Long id);
}

