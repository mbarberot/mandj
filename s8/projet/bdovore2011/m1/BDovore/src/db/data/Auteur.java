package db.data;


public class Auteur {
	
	
	private Album parentAlbum;
	
	private int id;
	private String pseudo, prenom, nom;
	
	
	public Auteur(Album parentAlbum, int id, String pseudo, String prenom, String nom) {
		this.parentAlbum = parentAlbum;
		this.id = id;
		this.pseudo = pseudo;
		this.prenom = prenom;
		this.nom = nom;
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
	
	
	public Album getParentAlbum() {
		return parentAlbum;
	}
}
