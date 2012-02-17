package db.data;


public class Serie {
	
	private int id;
	private String nom;
	
	private String genre;
	private Integer nbTomes; // Nullable (aucun ? one-shot ?)
	private int flg_fini; // Nullable (cf après)
	
	private String histoire;
	
	
	
	/* Constantes pour FLG_FINI */
	public static int SERIE_NA = 0;
	public static int SERIE_ENCOURS = 1;
	public static int SERIE_FINIE = 2;
	public static int SERIE_INTERROMPUE = 3;
	
	
	
	
	public Serie(int id, String nom, String genre) {
		this.id = id;
		this.nom = nom;
		this.genre = genre;
		this.nbTomes = 0;
		this.flg_fini = 0;
		this.histoire = "";
	}

	public void completeSerie(int nbTomes, int flg_fini, String histoire){
		this.nbTomes = nbTomes;
		this.flg_fini = flg_fini;
		this.histoire = histoire;
	}

	public int getId() {
		return id;
	}
	public String getNom() {
		return nom;
	}
	public String getGenre() {
		return genre;
	}
	public Integer getNbTomes() {
		return nbTomes;
	}
	public Integer getFlg_fini() {
		return flg_fini;
	}
	public String getHistoire() {
		return histoire;
	}
}
