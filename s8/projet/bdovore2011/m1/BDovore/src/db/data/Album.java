package db.data;

import java.util.ArrayList;
import java.util.Iterator;


public class Album {
	
	private int id;
	private String titre;
	private Integer numTome; // nullable
	
	private Integer idSerie;
	private String serie;
	
	private Integer idGenre;
	private String genre;
	
	private ArrayList<Auteur> scenaristes; // empty-able
	private ArrayList<Auteur> dessinateurs; // empty-able
	private ArrayList<Auteur> coloristes; // empty-able
	
	private boolean integrale, coffret;
	
	private ArrayList<Edition> editions; // empty-able
	
	// Uniquement utile pour affichage en liste (avant d'avoir tout)
	private String defaultISBN;
	private String defaultEAN;
	
	
	
	
	
	public Album(int id, String titre, Integer numTome, Integer idSerie, String serie,
			Integer idGenre, String genre, String integrale, Integer flg_type,
			String defaultISBN, String defaultEAN) {
		this.id = id;
		this.titre = titre;
		this.numTome = numTome;
		this.idSerie = idSerie;
		this.serie = serie;
		this.idGenre = idGenre;
		this.genre = genre;
		
		// TODO : Gestion avec flg_type dans la V2
		this.integrale = (integrale.equals("O") || !integrale.equals("N"));
		this.coffret = flg_type == 1;
		
		
		this.defaultISBN = defaultISBN;
		this.defaultEAN = defaultEAN;
		
		
		this.scenaristes = new ArrayList<Auteur>();
		this.dessinateurs = new ArrayList<Auteur>();
		this.coloristes = new ArrayList<Auteur>();
		this.editions = new ArrayList<Edition>();
	}
	
	
	
	
	
	public ArrayList<Auteur> getScenaristes() {
		return scenaristes;
	}
	public void setScenaristes(ArrayList<Auteur> scenaristes) {
		this.scenaristes = scenaristes;
	}
	
	public ArrayList<Auteur> getDessinateurs() {
		return dessinateurs;
	}
	public void setDessinateurs(ArrayList<Auteur> dessinateurs) {
		this.dessinateurs = dessinateurs;
	}
	
	public ArrayList<Auteur> getColoristes() {
		return coloristes;
	}
	public void setColoristes(ArrayList<Auteur> coloristes) {
		this.coloristes = coloristes;
	}
	
	public ArrayList<Edition> getEditions() {
		return editions;
	}
	public void setEditions(ArrayList<Edition> editions) {
		this.editions = editions;
	}
	
	public int getId() {
		return id;
	}
	public String getTitre() {
		return titre;
	}
	public Integer getNumTome() {
		return numTome;
	}
	public Integer getIdSerie() {
		return idSerie;
	}
	public String getSerie() {
		return serie;
	}
	public Integer getIdGenre() {
		return idGenre;
	}
	public String getGenre() {
		return genre;
	}
	public boolean isIntegrale() {
		return integrale;
	}
	public boolean isCoffret() {
		return coffret;
	}
	public String getDefaultISBN() {
		return defaultISBN;
	}
	public String getDefaultEAN() {
		return defaultEAN;
	}
	
	
	
	
	public boolean isPossede() {
		for (Iterator<Edition> i = editions.iterator(); i.hasNext();) {
			Edition edition = (Edition) i.next();
			if (edition.isPossede()) return true;
		}
		
		return false;
	}
}
