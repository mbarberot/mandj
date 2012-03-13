package db.data;

import java.sql.Date;

/**
 * Classe décrivant un auteur.
 */
public class Auteur {

    private Album parentAlbum;
    private int id;
    private String pseudo, prenom, nom, nationalite;
    private Date dteNaissance, dteDeces;

    public Auteur(Album parentAlbum, int id, String pseudo, String prenom, String nom) {
        this.parentAlbum = parentAlbum;
        this.id = id;
        this.pseudo = pseudo;
        this.prenom = prenom;
        this.nom = nom;
        this.nationalite = "";
        this.dteNaissance = null;
        this.dteDeces = null;
    }

    public void completeAuteur(Date dteNaissance, Date dteDeces, String nationalite) {
        this.nationalite = nationalite;
        this.dteNaissance = dteNaissance;
        this.dteDeces = dteDeces;
    }

    public Album getParentAlbum() {
        return parentAlbum;
    }

    public int getId() {
        return id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getNationalite() {
        return nationalite;
    }

    public Date getDteNaissance() {
        return dteNaissance;
    }

    public Date getDteDeces() {
        return dteDeces;
    }

    // Pour JList (sinon affichage de l'adresse
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}