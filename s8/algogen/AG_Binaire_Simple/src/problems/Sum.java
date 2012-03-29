package problems;

import base.variables.Individual;

public class Sum extends Problem{
	
	public Sum(){
		super();
		problemName_ = "Sum";
		numberOfVariables_ = 10;
		
		lowerLimit_ = new double [numberOfVariables_];
		upperLimit_ = new double [numberOfVariables_];
		precision_ = new int [numberOfVariables_];
		

		for (int i = 0; i < numberOfVariables_; i++){
			lowerLimit_[i] = 0.0 ;
			upperLimit_[i] = 100.0;
			precision_[i] = 10;
		}
		
		super.setObjective("maximize");
	}

	@Override
	public void evaluate(Individual individual) {
		
		double f = 0.0;
		
		for (int i = 0; i < individual.getChromosome().length(); i++){
			f += individual.getAllele(i).getDoubleValue();
		}
		
		individual.setFitness(f);
	}

}
