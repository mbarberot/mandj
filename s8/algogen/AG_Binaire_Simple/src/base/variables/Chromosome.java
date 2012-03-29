package base.variables;

import java.util.BitSet;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * September 2009
 * 
 * last update: March 2011
 * 
 * 
 * A chromosome (also sometimes called a genome) is a set of parameters which define 
 * a proposed solution to the problem that the genetic algorithm is trying to solve.
 * Source:  http://en.wikipedia.org/wiki/Chromosome_(genetic_algorithm)
 * ------
 */

public class Chromosome {

	private Genotype genotype;

	private Allele[] alleles;


	public Chromosome(Genotype genotype) {
		this.genotype = genotype;
		alleles = new Allele[genotype.length()];
		for (int i = 0; i < alleles.length; i++) {
			alleles[i] = new Allele(genotype.getGene(i));
		}
	}

	/**
	 * Returns the i-th allele of this chromosome.
	 * 
	 * @param i the index of the targeted allele in the chromosome
	 * @return the allele
	 */
	public Allele getAllele(int i) {
		return alleles[i];
	}

	/**
	 * Change the real value of the i-th allele.
	 * 
	 * @param i the index
	 * @param toCopy the value to set
	 */
	public void setIntAllele(int i, int value) {
		alleles[i].setIntValue(value);
	}

	public void setDoubleAllele(int i, Double value) {
		alleles[i].setDoubleValue(value);
	}

	public void setBitAllele(int i, BitSet value) {
		alleles[i].setBitValue(value);
	}

	public void setStringAllele(int i, String value) {
		alleles[i].setStringValue(value);
	}

	/**
	 * Returns the number of alleles for this chromosome.
	 * 
	 * @return the number of alleles
	 */
	public int length() {

		return alleles.length;

	}

	/**
	 * Returns the genotype of this chromosome.
	 * 
	 * @return Returns the genotype.
	 */
	public Genotype getGenotype() {
		return genotype;
	}

}