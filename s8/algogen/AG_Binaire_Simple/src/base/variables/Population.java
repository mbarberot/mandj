package base.variables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 * 
 * September 2009
 * 
 * Last update : March 2011
 * 
 * A population is a set of solutions (individuals) of the problem to be optimized 
 * (by the genetic algorithm).
 * 
 */

public class Population {
	/*
	 * The genotype of the individuals in this population.
	 */
	private Genotype genotype;
	
	//private ObjectiveFunctions objectives;
	
	/*
	 * The list of all individuals in this population.
	 */
	protected List<Individual> individuals;

	/**
	 * Constructs a population of the given size with individuals of the given
	 * genotype.
	 * 
	 * Alleles of individuals are initialized at random by the constructor of
	 * class Allele.
	 * 
	 * @param size
	 */
	public Population(int popsize, Genotype genotype) {
		this.genotype = genotype;
		individuals = new ArrayList<Individual>();
		for (int i = 0; i < popsize; i++) {
			individuals.add(new Individual(genotype));
		}
		
	}

	/**
	 * Returns the i-th individual of this population.
	 * 
	 * @param index
	 * @return
	 */
	public Individual getIndividual(int index) {

		return individuals.get(index);

	}

	/**
	 * Replaces the i-th individual of this population with the specified element.
	 * 
	 * @param individual
	 */
	public void setIndividual(int index, Individual individual) {
		individuals.set(index, individual);
		
	}

	/**
	 * Returns the number of individuals in this population.
	 * 
	 * @return the size of the population
	 */
	public int size() {
		return individuals.size();
	}

	/**
	 * Returns the genotype of this population's individuals.
	 * 
	 * @return Returns the genotype.
	 */
	public Genotype getGenotype() {
		return genotype;
	}


	/**
	 * 
	 */
	public String toString() {
		
		String s = "";
		for (Individual indiv : individuals) {
			s += indiv.toString() + "\n";
		}

		return s;
	}

	/**
	 * 
	 * @return
	 */
	public List<Individual> getAllIndividuals() {
		return individuals;
	}

	/**
	 * 
	 * @param randomizer
	 * @return
	 */
	public Individual getIndividualAtRandom(Random randomizer) {
		int id = randomizer.nextInt(this.size());
		
		return getIndividual(id);
	}
	/**
	 * 
	 * @param indiv
	 */
	public void add(Individual indiv) {
		individuals.add(indiv);
	}
	/**
	 * 
	 * @param indiv
	 */
	public void remove(Individual indiv) {
		individuals.remove(indiv);
	}

}