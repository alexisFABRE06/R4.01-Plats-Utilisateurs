package infrastructure;

/**
 * DTO pour créer ou modifier un utilisateur.
 * Le champ dateInscription est géré par le serveur.
 */
public class UtilisateurInput {
    public String nom;
    public String prenom;
    public String email;
    public String telephone;
    public String adresseDefaut;

    public UtilisateurInput() {}

    public UtilisateurInput(String nom, String prenom, String email, String telephone, String adresseDefaut) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresseDefaut = adresseDefaut;
    }
}
