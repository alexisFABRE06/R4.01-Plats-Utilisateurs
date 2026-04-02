package domain;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur (abonné) de l'application.
 * Stocke les informations personnelles et de contact de l'utilisateur.
 */
public class Utilisateur {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresseDefaut;
    private LocalDateTime dateInscription;

    public Utilisateur() {} // Nécessaire pour Jakarta JSON Binding

    // Constructeur avec paramètres
    public Utilisateur(String nom, String prenom, String email, String telephone, String adresseDefaut) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresseDefaut = adresseDefaut;
        this.dateInscription = LocalDateTime.now();
    }

    // Getters et Setters
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresseDefaut() {
        return adresseDefaut;
    }

    public void setAdresseDefaut(String adresseDefaut) {
        this.adresseDefaut = adresseDefaut;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }
}
