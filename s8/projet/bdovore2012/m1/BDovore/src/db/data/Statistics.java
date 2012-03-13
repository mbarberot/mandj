package db.data;

import java.util.ArrayList;

/**
 * Classe contenant toutes les statistiques
 *
 * @author Thorisoka
 */
public class Statistics {

    /*
     * Totaux :
     */
    private int total;
    private int owned;
    private int wanted;
    
    /*
     * Statistiques 
     */
    private ArrayList<StatisticsRepartition> genres;
    private ArrayList<StatisticsRepartition> editeurs;
    private ArrayList<StatisticsRepartition> dessinateurs;
    private ArrayList<StatisticsRepartition> scenaristes;

    
    /**
     * Constructeur de la classe
     *
     * @param total         Nombre total de tomes
     * @param owned         Nombre de tomes possédés 
     * @param wanted        Nombre de tomes souhaités
     * @param genres        Répartition des genres parmis les tomes
     * @param editeurs      Répartition des éditeurs parmis les tomes
     * @param dessinateurs  Répartition des dessinateurs parmis les tomes
     * @param scenaristes   Répartition des scénaristes parmis les tomes
     */
    public Statistics(int total, int owned, int wanted, ArrayList<StatisticsRepartition> genres,
            ArrayList<StatisticsRepartition> editeurs,
            ArrayList<StatisticsRepartition> dessinateurs,
            ArrayList<StatisticsRepartition> scenaristes) {
        this.total = total;
        this.owned = owned;
        this.wanted = wanted;
        this.genres = genres;
        this.editeurs = editeurs;
        this.dessinateurs = dessinateurs;
        this.scenaristes = scenaristes;
    }

    /*
     * Getters
     */
    
    public int getTotal() {
        return total;
    }

    public int getOwned() {
        return owned;
    }

    public int getWanted() {
        return wanted;
    }

    public ArrayList<StatisticsRepartition> getGenres() {
        return genres;
    }

    public ArrayList<StatisticsRepartition> getEditeurs() {
        return editeurs;
    }

    public ArrayList<StatisticsRepartition> getDessinateurs() {
        return dessinateurs;
    }

    public ArrayList<StatisticsRepartition> getScenaristes() {
        return scenaristes;
    }
}
