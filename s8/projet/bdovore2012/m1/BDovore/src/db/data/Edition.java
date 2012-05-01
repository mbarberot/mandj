package db.data;

import db.CodeBarre;
import java.sql.Date;
import main.Config;

public class Edition {

    /*
     * Informations sur l'édition
     */
    private Album parentAlbum;
    private int id;
    private boolean defaut;
    private Integer idEditeur;
    private String editeur;
    private Date parution;
    private Integer idCollection;
    private String collection;
    private String isbn, ean;
    private String couverture;
    private boolean editionOriginale, tirageTete;
    private String commentaires;
    private boolean possede;
    private boolean aAcheter;
    private boolean dedicace;
    private boolean pret;
    private int update;
    /*
     * Constantes
     */
    public static final int DO_NOTHING = 0;
    public static final int INSERT = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    
    /**
     * Retourne un chaîne correspondant à l'action donnée.
     * 
     * @param action Une des constantes d'Edition : DO_NOTHING, INSERT, UPDATE ou DELETE
     * @return Une chaîne de caractère, respectivement "Rien", "Ajout", "Mise à jour" ou "Suppression"
     */
    public static String getStringForAction(int action)
    {
        String str = "";

        switch (action)
        {
            case Edition.INSERT:
                str = "Ajout";
                break;
            case Edition.UPDATE:
                str = "Mise à jour";
                break;
            case Edition.DELETE:
                str = "Suppression";
                break;
            case Edition.DO_NOTHING:
                str = "Aucune action";
                break;
        }
        return str;
    }

    /**
     * Constructeur de la classe
     *
     * @param parentAlbum
     * @param id ID de l'édition
     * @param idEditeur ID de l'éditeur
     * @param editeur Nom de l'éditeur
     * @param parution Date de parution
     * @param isbn ISBN du tome
     * @param couverture Nom de l'image de couverture
     * @param flgDefault
     */
    public Edition(Album parentAlbum, int id, Integer idEditeur,
            String editeur, Date parution, String isbn, String couverture, Boolean flgDefault) {
        this.parentAlbum = parentAlbum;
        this.id = id;
        this.defaut = flgDefault;
        this.idEditeur = idEditeur;
        this.editeur = editeur;
        this.parution = parution;
        this.idCollection = null; //idCollection;
        this.collection = ""; //collection;

        this.isbn = "";
        this.ean = "";

        // Si l'isbn récupéré est un code ISBN10, on le converti pour avoir aussi le 13
        if (CodeBarre.isCodeEAN10(isbn)) {
            System.out.println("isbn 10");
            this.isbn = isbn;
            this.ean = CodeBarre.toBDovoreEAN(isbn);
            // Sinon si c'est un code ISBN13, on garde le 13
        } else if (CodeBarre.isCodeEAN13(isbn)) {
            System.out.println("isbn 13");
            this.ean = isbn;
        }

        this.couverture = couverture;
        this.editionOriginale = false; //(editionOriginale.equals("O") || !editionOriginale.equals("N"));
        this.tirageTete = false; //(tirageTete.equals("O") || !tirageTete.equals("N"));
        this.commentaires = ""; //commentaires;
    }

    /*
     * Getters
     */
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
        return couverture;
    }

    public String getCouvertureURL() {
        return getCouverture();
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

    public boolean isAAcheter() {
        return aAcheter;
    }

    public boolean isDedicace() {
        return dedicace;
    }

    public boolean isPret() {
        return pret;
    }

    public int getUpdate() {
        return update;
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

    /*
     * Setters
     */
    public void setPossede(boolean possede) {
        this.possede = possede;
    }

    public void setDedicace(boolean dedicace) {
        this.dedicace = dedicace;
    }

    public void setAAcheter(boolean acheter) {
        aAcheter = acheter;
    }

    public void setPret(boolean pret) {
        this.pret = pret;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    @Override
    public String toString() {
        return "Mode = " + update + " / " + possede + "/" + aAcheter + "/" + dedicace + "/" + pret;
    }
}
