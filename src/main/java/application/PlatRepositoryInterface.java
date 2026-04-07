package application;

import domain.Plat;
import infrastructure.PlatInput;

import java.util.List;
import java.util.Optional;

/**
 * Contrat d'acces aux donnees des plats.
 */
public interface PlatRepositoryInterface {
    List<Plat> findAll();

    Optional<Plat> findById(Long id);

    Plat create(PlatInput input);

    Optional<Plat> update(Long id, PlatInput input);

    boolean delete(Long id);
}

