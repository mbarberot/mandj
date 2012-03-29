package base.variables;

import java.util.BitSet;


/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 * 
 * September 2009
 * 
 * Last update: March 2011
 * 
 * 
 * An individual can be considered as any possible solution of the optimization problem
 * 
 */

public class Individual {

	private Chromosome chromosome;

	/*
	 * A unique number that identifies the individual
	 */
	private int id = 0;

	private double fitness;

	/*
	 * isFeasible becomes true if the individual respects the constraints and flase otherwise 
	 */


	/*
	 * Has the fitness of this individual been calculated?
	 */
	private boolean isIndivEvaluated = false;

	/*
	 * Is the solution valid for the considered problem.
	 */
	/**
	 * Constructs a new individual from the given genotype. An individual
	 * created this way is its own parents and has an initial fitness of 0.
	 * 
	 * @param genotype
	 */
	public Individual(Genotype genotype) {
		chromosome = new Chromosome(genotype);
	}

	public Individual(Individual indiv){

		this(indiv.getChromosome().getGenotype());
		for (int i = 0; i < indiv.getChromosome().length(); i++){
			this.getAllele(i).setBitValue(indiv.getAllele(i).getBitValue());
		}

		this.isIndivEvaluated = indiv.isEvaluated();
	}


	/*
	 * Check if two given individuals are equal (ie all their allèles are equal)
	 */

	public boolean equals(Individual _indiv){
		for(int i = 0; i < _indiv.getChromosome().length(); i++){
			if (this.getAllele(i).getBitValue() != _indiv.getAllele(i).getBitValue()){
				return false;
			}
		}
		return true;
	}


	/*
	 * 
	 */
	public void setId(int _id){
		this.id = _id;
	}

	public int getId(){
		return this.id;
	}

	public void setFitness(double f){
		fitness = f;
	}
	
	public double getFitness(){
		return fitness;
	}

	public void setEvaluated(boolean bool) {
		this.isIndivEvaluated = bool;
	}

	public boolean isEvaluated(){
		return isIndivEvaluated;
	}

	public Chromosome getChromosome() {
		return chromosome;
	}

	/**
	 * Returns the i-th allele of this individual's chromosome.
	 * 
	 * @return the i-th allele
	 */
	public Allele getAllele(int i) {
		return chromosome.getAllele(i);
	}

	/**
	 * Sets the value of the i-th allele of this individual's chromosome.
	 * 
	 * @param i
	 *            the locus of the allele
	 * @param value
	 *            the value of the allele to set
	 */
	public void setIntAllele(int i, int newValue) {
		chromosome.setIntAllele(i, newValue);
	}

	public void setDoubleAllele(int i, Double newValue) {
		chromosome.setDoubleAllele(i, newValue);
	}

	public void setBitAllele(int i, BitSet newValue) {
		chromosome.setBitAllele(i, newValue);
	}

	public void setStringAllele(int i, String newValue) {
		chromosome.setStringAllele(i, newValue);
	}


	/**
	 * 
	 */
	public String toString(){
		//String s = new String(this.getId() + "\t");
		String s = "";
		for (int i = 0; i < chromosome.length(); i++){
			//s += this.getAllele(i).getDoubleValue() + " ";
			s += this.getAllele(i).getIntValue() + " ";
		}
		s += "** \t" + getFitness();
		
		return s;
	}

	/**
	 * Copy the current individual
	 */
	public Individual copy(){
		return new Individual(this);
	}
}