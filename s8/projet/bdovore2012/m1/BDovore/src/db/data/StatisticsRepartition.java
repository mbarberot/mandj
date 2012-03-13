package db.data;

/**
 * Contient les éléments d'une répartition (par genre, édition, etc)
 *
 * @author Thorisoka
 */
public class StatisticsRepartition {

    /*
     *
     */
    private String nom;
    private int nombre;
    private int ratio;

    /**
     * Constructeur de la classe
     *
     * @param nom Nom de l'auteur, du genre, ...
     * @param nombre Nombre de tomes dans lequel il ou elle apparait
     * @param ratio Pourcentage par rapport au nombre total des tomes
     */
    public StatisticsRepartition(String nom, int nombre, int ratio) {
        this.nom = nom;
        this.nombre = nombre;
        this.ratio = ratio;
    }

    /*
     * Getters
     */
    public String getNom() {
        return nom;
    }

    public int getNombre() {
        return nombre;
    }

    public int getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return nom + "  --  " + nombre + " / " + ratio + "%";
    }
}
