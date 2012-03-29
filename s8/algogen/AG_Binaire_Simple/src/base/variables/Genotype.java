package base.variables;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import problems.Problem;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * September 2009
 * 
 * last update: March 2011
 * 
 * 
 * A genotype is a set of genes. 
 * 
 */

public class Genotype {

	private Gene[] genesVector;

	/**
	 * Instanciates a genotype from an array of genes.
	 * 
	 * @param genes (ie an array of genes)
	 */

	public Genotype(Problem problem) {
		int nbGenes = problem.getNumberOfVariables();
		genesVector = new Gene[nbGenes];

		for (int i = 0; i < nbGenes; i++){
			genesVector[i] = new Gene(problem.getLowerLimit(i), 
					problem.getUpperLimit(i),					
					problem.getPrecision(i));					
		}
	}

	/**
	 * Return the number of genes of this genotype.
	 * 
	 * @return the number of genes
	 */
	public int length() {
		return genesVector.length;
	}

	/**
	 * Returns the i-th gene of this genotype.
	 * 
	 * @param i
	 * @return the i-th gene
	 */
	public Gene getGene(int i) {
		return genesVector[i];
	}


	/**
	 * @return Returns the different information relative to 
	 * the genes (type, lower and upper bounds) that constitue the genotype
	 */
	public String toString() {
		String s = "";
		for (int i = 0; i < length(); i++) {
			s += genesVector[i].toString() + "\n";
		}
		return s;
	}

}