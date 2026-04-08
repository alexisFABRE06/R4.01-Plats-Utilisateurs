package domain;

import java.time.LocalDateTime;

/**
 * Représente un plat proposé par l'entreprise de livraison.
 * Contient les informations essentielles du plat: nom, description et prix.
 */
public class Plat {
    private Long id;
    private String nom;
    private String description;
    private Double prix;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    public Plat() {} // Nécessaire pour Jakarta JSON Binding

    public Plat(String nom, String description, Double prix) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.dateCreation = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
