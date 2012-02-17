package db.data;

import java.sql.Date;


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
	
	public void completeAuteur(Date dteNaissance, Date dteDeces, String nationalite){
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

	// to string réalisé pour l'utilisation de JList sur les auteur dans la fiche d'un album
	// Si cette fonction n'existe pas, JList affiche l'adresse de l'objet et non pas le nom et prenom
	public String toString(){
		return prenom+" "+nom; 
	}
}