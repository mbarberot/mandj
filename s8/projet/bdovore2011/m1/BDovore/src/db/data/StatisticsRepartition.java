package db.data;


/**
 * Contient les éléments d'une répartition (par genre, édition, etc)
 * @author Thorisoka
 */
public class StatisticsRepartition {
	
	private String nom;
	private int nombre;
	private int ratio;
	
	public StatisticsRepartition(String nom, int nombre, int ratio) {
		this.nom = nom;
		this.nombre = nombre;
		this.ratio = ratio;
	}
	
	
	public String getNom() {
		return nom;
	}
	public int getNombre() {
		return nombre;
	}
	public int getRatio() {
		return ratio;
	}
	
	
	public String toString() {
		return nom + "  --  " + nombre + " / " + ratio + "%";
	}
}
