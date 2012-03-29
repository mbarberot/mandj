package base.operators.crossover;

import base.variables.Individual;


/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 * 
 * 
 * March 2011
 * 
 */

public class OnePointCrossover extends Crossover{

	public OnePointCrossover(Individual parent1, Individual parent2) {
		super(parent1, parent2);
	}

	public OnePointCrossover(Individual parent1, Individual parent2, float xProbability) {
		super(parent1, parent2, xProbability);
	}

	@Override
	public void doCross(Individual parent1, Individual parent2) {

		double rand = Math.random();
		if (rand <= getXProbability()){
			String o1 = "";
			String o2 = "";

			int cutOffGene = (int) (Math.random()*parent1.getChromosome().length());
			int nbBits = parent1.getChromosome().getGenotype().getGene(cutOffGene).getNbBits();
			int cutOffPoint = (int) (Math.random()*nbBits);

			for (int i = 0; i < cutOffGene; i++){
				offspring1.setStringAllele(i, parent1.getAllele(i).getStringValue());
				offspring2.setStringAllele(i, parent2.getAllele(i).getStringValue());
			}

			for (int i = 0; i < cutOffPoint; i++){
				o1 += parent1.getAllele(cutOffGene).getStringValue().charAt(i);
				o2 += parent2.getAllele(cutOffGene).getStringValue().charAt(i);
			}

			for (int i = cutOffPoint; i < nbBits; i++){
				o1 += parent2.getAllele(cutOffGene).getStringValue().charAt(i);
				o2 += parent1.getAllele(cutOffGene).getStringValue().charAt(i);
			}

			offspring1.setStringAllele(cutOffGene, o1);
			offspring2.setStringAllele(cutOffGene, o2);

			for (int i = cutOffGene; i < parent1.getChromosome().length(); i++){
				offspring1.setStringAllele(i, parent2.getAllele(i).getStringValue());
				offspring2.setStringAllele(i, parent1.getAllele(i).getStringValue());
			}

		}else{
			offspring1 = parent1.copy();
			offspring2 = parent2.copy();
		}
	}
}
