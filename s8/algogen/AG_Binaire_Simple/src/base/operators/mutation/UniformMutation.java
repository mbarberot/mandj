package base.operators.mutation;

import base.variables.Individual;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * 
 * June 2010
 * 
 * Lastupdate : March 2011
 */

public class UniformMutation extends Mutation{

	public UniformMutation(Individual individual) {
		super(individual);
	}
	
	public UniformMutation(Individual individual, float probability) {
		super(individual, probability);
	}

	@Override
	public Individual doMutate() {
		
		double rand = Math.random();
		
		if(rand <= getProbability()) {
			
			// Choisir un allèle au hasard
			int allele = (int)(Math.random() * individual.getChromosome().length());
			// Le faire muter
			int bit = (int)(Math.random() * individual.getAllele(allele).getGene().getNbBits());
			StringBuffer nAllele = new StringBuffer(individual.getAllele(allele).getStringValue());
			
			// On interchange le bit trouvé
			if (nAllele.charAt(bit) == '0')
			{
				nAllele.setCharAt(bit, '1');
			}
			else
			{
				nAllele.setCharAt(bit, '0');
			}
			
			// On remplace l'allèle
			individual.setStringAllele(allele, nAllele.toString());
		
		}
		
		return individual;
	}

}
