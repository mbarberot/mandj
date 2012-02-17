package db.data;

import java.sql.Date;
import main.Config;


public class Edition {
	
	
	private Album parentAlbum;
	
	
	private int id;
	//private int idTome;
	private boolean defaut;
	
	private Integer idEditeur;
	private String editeur;
	private Date parution;
	
	private Integer idCollection;
	private String collection;
	
	private String isbn,ean;
	private String couverture;
	private boolean editionOriginale, tirageTete;
	private String commentaires;
	
	
	
	private boolean possede;
	private boolean aAcheter;
	private boolean dedicace;
	private boolean pret;
	
	private int update;
	public static final int DO_NOTHING = 0;
	public static final int INSERT = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
	
	//private String pretQui;
	
	
	
	public Edition(Album parentAlbum, int id, String defaut, Integer idEditeur,
			String editeur, Date parution, Integer idCollection, String collection,
			String isbn, String ean, String couverture, String editionOriginale,
			String tirageTete, String commentaires) {
		this.parentAlbum = parentAlbum;
		this.id = id;
		this.defaut = (defaut.equals("O") || !defaut.equals("N"));
		this.idEditeur = idEditeur;
		this.editeur = editeur;
		this.parution = parution;
		this.idCollection = idCollection;
		this.collection = collection;
		this.isbn = isbn;
		this.ean = ean;
		this.couverture = couverture;
		this.editionOriginale = (editionOriginale.equals("O") ||
			!editionOriginale.equals("N"));
		this.tirageTete = (tirageTete.equals("O") || !tirageTete.equals("N"));
		this.commentaires = commentaires;
	}
	
	
	
	public int getId() {
		return id;
	}
	public boolean isDefaut() {
		return defaut;
	}
	public String getEditeur() {
		return editeur;
	}
	public Date getParution() {
		return parution;
	}
	public String getCollection() {
		return collection;
	}
	public String getISBN() {
		return isbn;
	}
	public String getEAN() {
		return ean;
	}
	public String getCouverture() {
		//return "CV-045497-044819.gif";
		return couverture;
	}
	
	public String getCouvertureURL() {
		//return Config.IMG_COUV_URL + "CV-045497-044819.gif";
		return Config.IMG_COUV_URL + getCouverture();
	}
	
	public boolean isEditionOriginale() {
		return editionOriginale;
	}
	public boolean isTirageTete() {
		return tirageTete;
	}
	public String getCommentaires() {
		return commentaires;
	}
	
	
	
	
	public boolean isPossede() {
		return possede;
	}
	public void setPossede(boolean possede) {
		this.possede = possede;
	}
	public boolean isAAcheter() {
		return aAcheter;
	}
	public void setAAcheter(boolean acheter) {
		aAcheter = acheter;
	}
	public boolean isDedicace() {
		return dedicace;
	}
	public void setDedicace(boolean dedicace) {
		this.dedicace = dedicace;
	}
	public boolean isPret() {
		return pret;
	}
	public void setPret(boolean pret) {
		this.pret = pret;
	}
	
	
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
	
	
	public Album getParentAlbum() {
		return parentAlbum;
	}
	
	
	public Integer getIdEditeur() {
		return idEditeur;
	}
	
	public Integer getIdCollection() {
		return idCollection;
	}
	
	public String toString() {
		return "Mode = "+update+" / "+ possede+"/"+aAcheter+"/"+dedicace+"/"+pret;
	}
}
