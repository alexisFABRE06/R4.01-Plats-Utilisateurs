package infrastructure;

import application.UtilisateurRepositoryInterface;
import domain.Utilisateur;

import java.io.Closeable;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Repository JDBC pour les utilisateurs stockes sur MariaDB.
 */
public class UtilisateurRepositoryMariadb implements UtilisateurRepositoryInterface, Closeable {
    private Connection dbConnection;

    public UtilisateurRepositoryMariadb(String infoConnection, String user, String pwd)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        dbConnection = DriverManager.getConnection(infoConnection, user, pwd);
    }

    @Override
    public void close() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Utilisateur> getAllUtilisateurs() {
        String query = "SELECT id, nom, prenom, email, telephone, adresse_defaut, date_inscription FROM utilisateurs ORDER BY id";
        ArrayList<Utilisateur> utilisateurs = new ArrayList<>();

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
    public Utilisateur getUtilisateur(Long id) {
        String query = "SELECT id, nom, prenom, email, telephone, adresse_defaut, date_inscription FROM utilisateurs WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la lecture d'un utilisateur", e);
        }
        return null;
    }

    @Override
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        String query = "INSERT INTO utilisateurs (nom, prenom, email, telephone, adresse_defaut, date_inscription) VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement ps = dbConnection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getEmail());
            ps.setString(4, utilisateur.getTelephone());
            ps.setString(5, utilisateur.getAdresseDefaut());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);
                    return getUtilisateur(id);
                }
            }
            throw new RuntimeException("Creation de l'utilisateur sans cle generee");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur SQL lors de la creation d'un utilisateur", e);
        }
    }

    @Override
    public boolean updateUtilisateur(Long id, Utilisateur utilisateur) {
        String query = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, telephone=?, adresse_defaut=? WHERE id=?";

        try (PreparedStatement ps = dbConnection.prepareStatement(query)) {
            ps.setString(1, utilisateur.getNom());
            ps.setString(2, utilisateur.getPrenom());
            ps.setString(3, utilisateur.getEmail());
            ps.setString(4, utilisateur.getTelephone());
            ps.setString(5, utilisateur.getAdresseDefaut());
            ps.setLong(6, id);

            return ps.executeUpdate() > 0;
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

