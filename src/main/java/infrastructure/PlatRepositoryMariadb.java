package infrastructure;

import application.PlatRepositoryInterface;
import domain.Plat;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository JDBC pour les plats stockes sur MariaDB.
 */
public class PlatRepositoryMariadb implements PlatRepositoryInterface {
    private final Connection dbConnection;

    public PlatRepositoryMariadb(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Plat> findAll() {
        String query = "SELECT id, nom, description, prix, date_creation, date_modification FROM plats ORDER BY id";
        List<Plat> plats = new ArrayList<>();

        try (PreparedStatement ps = dbConnection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                plats.add(mapPlat(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la lecture des plats", e);
        }
        return plats;
    }

    @Override
    public Optional<Plat> findById(Long id) {
        String query = "SELECT id, nom, description, prix, date_creation, date_modification FROM plats WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapPlat(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la lecture d'un plat", e);
        }
        return Optional.empty();
    }

    @Override
    public Plat create(PlatInput input) {
        String query = "INSERT INTO plats (nom, description, prix, date_creation) VALUES (?, ?, ?, NOW())";

        try (PreparedStatement ps = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, input.nom);
            ps.setString(2, input.description);
            ps.setDouble(3, input.prix);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);
                    return findById(id).orElseThrow();
                }
            }
            throw new RuntimeException("Creation du plat sans cle generee");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la creation d'un plat", e);
        }
    }

    @Override
    public Optional<Plat> update(Long id, PlatInput input) {
        String query = "UPDATE plats SET nom=?, description=?, prix=?, date_modification=NOW() WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, input.nom);
            ps.setString(2, input.description);
            ps.setDouble(3, input.prix);
            ps.setLong(4, id);

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                return Optional.empty();
            }
            return findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la mise a jour d'un plat", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM plats WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la suppression d'un plat", e);
        }
    }

    private Plat mapPlat(ResultSet rs) throws SQLException {
        Plat plat = new Plat();
        plat.setId(rs.getLong("id"));
        plat.setNom(rs.getString("nom"));
        plat.setDescription(rs.getString("description"));
        plat.setPrix(rs.getDouble("prix"));
        plat.setDateCreation(toLocalDateTime(rs.getTimestamp("date_creation")));
        plat.setDateModification(toLocalDateTime(rs.getTimestamp("date_modification")));
        return plat;
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}

