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

public abstract class Mutation {

	protected Individual individual;
	protected float probability = (float) 0.01;
	
	public Mutation (Individual individual){
		this.individual = individual;
	}
	
	public Mutation (Individual individual, float probability){
		this.individual = individual;
		this.probability = probability;
	}
	
	public void setProbability(float probability){
		this.probability = probability;
	}
	
	public float getProbability(){
		return probability;
	}
	
	public abstract Individual doMutate();
}