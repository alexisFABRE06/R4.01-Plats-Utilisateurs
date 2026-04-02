package infrastructure;

/**
 * DTO pour créer ou modifier un plat.
 * Les champs dateCreation et dateModification sont gérés par le serveur.
 */
public class PlatInput {
    public String nom;
    public String description;
    public Double prix;

    public PlatInput() {}

    public PlatInput(String nom, String description, Double prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
    }
}
