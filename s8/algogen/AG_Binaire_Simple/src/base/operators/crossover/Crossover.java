package base.operators.crossover;

import base.variables.Individual;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * 
 * September 2009
 * 
 * Lastupdate : March 2011
 */

public abstract class Crossover {

	protected float xProbability = (float) 1.0;

	/**
	 * The genotype of the chromosomes to be mutated.
	 */

	private Individual parent1;
	private Individual parent2;

	protected Individual offspring1;
	protected Individual offspring2;

	/**
	 * Constructs a crosser from the given sga.
	 * 
	 * The genotype and crossover probability are retrieved from the sga.
	 */

	public Crossover(Individual parent1, Individual parent2) {
		this.parent1 = parent1;
		this.parent2 = parent2;
		offspring1 = new Individual(parent1);
		offspring2 = new Individual(parent2);
	}
	
	public Crossover(Individual parent1, Individual parent2, float xProbability) {
		this.parent1 = parent1;
		this.parent2 = parent2;
		offspring1 = new Individual(parent1);
		offspring2 = new Individual(parent2);
		this.xProbability = xProbability;
	}
	
	public abstract void doCross(Individual parent1, Individual  parent2);

	
	/**
	 * Sets the crossover probability.
	 * 
	 * @param crossProbability
	 *            The Crossover Probability to set.
	 */
	public void setCrossProbability(double crossProbability) {
		this.xProbability = (float) crossProbability;
	}
	
	/**
	 * Returns the crossover probability.
	 * 
	 * @return Returns the Crossover Probability.
	 */
	public double getXProbability() {
		return xProbability;
	}

	public Individual getOffspring(int i){
		if (i == 1) return offspring1.copy(); 
		else return offspring2.copy();
	}
	
}