package infrastructure;

import application.UtilisateurRepositoryInterface;
import domain.Utilisateur;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository JDBC pour les utilisateurs stockes sur MariaDB.
 */
public class UtilisateurRepositoryMariadb implements UtilisateurRepositoryInterface {
    private final Connection dbConnection;

    public UtilisateurRepositoryMariadb(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Utilisateur> findAll() {
        String query = "SELECT id, nom, prenom, email, telephone, adresse_defaut, date_inscription FROM utilisateurs ORDER BY id";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (PreparedStatement ps = dbConnection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(mapUtilisateur(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la lecture des utilisateurs", e);
        }
        return utilisateurs;
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        String query = "SELECT id, nom, prenom, email, telephone, adresse_defaut, date_inscription FROM utilisateurs WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la lecture d'un utilisateur", e);
        }
        return Optional.empty();
    }

    @Override
    public Utilisateur create(UtilisateurInput input) {
        String query = "INSERT INTO utilisateurs (nom, prenom, email, telephone, adresse_defaut, date_inscription) VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement ps = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, input.nom);
            ps.setString(2, input.prenom);
            ps.setString(3, input.email);
            ps.setString(4, input.telephone);
            ps.setString(5, input.adresseDefaut);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);
                    return findById(id).orElseThrow();
                }
            }
            throw new RuntimeException("Creation de l'utilisateur sans cle generee");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la creation d'un utilisateur", e);
        }
    }

    @Override
    public Optional<Utilisateur> update(Long id, UtilisateurInput input) {
        String query = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, telephone=?, adresse_defaut=? WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, input.nom);
            ps.setString(2, input.prenom);
            ps.setString(3, input.email);
            ps.setString(4, input.telephone);
            ps.setString(5, input.adresseDefaut);
            ps.setLong(6, id);

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                return Optional.empty();
            }
            return findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la mise a jour d'un utilisateur", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM utilisateurs WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la suppression d'un utilisateur", e);
        }
    }

    private Utilisateur mapUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getLong("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setTelephone(rs.getString("telephone"));
        utilisateur.setAdresseDefaut(rs.getString("adresse_defaut"));
        utilisateur.setDateInscription(toLocalDateTime(rs.getTimestamp("date_inscription")));
        return utilisateur;
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }
}

