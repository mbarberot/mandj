package db.data;

/**
 * Classe codant une série
 *
 */
public class Serie {

    /*
     * Données sur la série
     */
    private int id;
    private String nom;
    private String genre;
    private Integer nbTomes; // Nullable (aucun ? one-shot ?)
    private int flg_fini; // Nullable (cf après)
    private String histoire;
    
    /*
     * Constantes pour FLG_FINI
     */
    public static int SERIE_NA = 0;
    public static int SERIE_ENCOURS = 1;
    public static int SERIE_FINIE = 2;
    public static int SERIE_INTERROMPUE = 3;

    /**
     * Constructeur de la classe
     * 
     * @param id    ID de la série
     * @param nom   Nom de la série
     * @param genre Genre de la série
     */
    public Serie(int id, String nom, String genre) {
        this.id = id;
        this.nom = nom;
        this.genre = genre;
        this.nbTomes = 0;
        this.flg_fini = 0;
        this.histoire = "";
    }

    /**
     * Complète les données de la série
     * 
     * @param nbTomes   Nombre de tomes de la série
     * @param flg_fini  'true' si la série est finie, 'false' sinon
     * @param histoire  Synopsis de la série
     */
    public void completeSerie(int nbTomes, int flg_fini, String histoire) {
        this.nbTomes = nbTomes;
        this.flg_fini = flg_fini;
        this.histoire = histoire;
    }

    /*
     * Getters
     */
    
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
