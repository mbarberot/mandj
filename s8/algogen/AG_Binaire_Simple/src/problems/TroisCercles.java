package problems;

import base.variables.Individual;

public class TroisCercles extends Problem{

	public TroisCercles()
	{
		super();	
		numberOfVariables_ = 2;		  
		problemName_ = "Trois Cercles";
		lowerLimit_ = new double [numberOfVariables_];
		upperLimit_ = new double [numberOfVariables_];
		precision_  = new int [numberOfVariables_];	
		
		// Paramètres de A
		lowerLimit_[0] = 10.0;
		upperLimit_[0] = 80.0;
		precision_[0] = 2;
		
		// Paramètres de B
		lowerLimit_[1] = 0;
		upperLimit_[1] = 100;
		precision_[1] = 2;
                
                isToMinimize = false;
		
	}

	@Override
	public void evaluate(Individual individual) {
		double area = 0.0;
		double a, b, c;
		
		a = (individual.getAllele(0).getDoubleValue() * 70.0 /1023) + 10.0 ;
		b = (individual.getAllele(1).getDoubleValue() * (80.0 - a) / 127) + 10.0 ;
		c = 100 - (a+b);
		area = Math.PI/2 * (Math.pow(100, 2) - (Math.pow(a, 2) + Math.pow(b, 2) + Math.pow(c, 2))); 
		individual.setFitness(area);
		
		
	}
        
        
}
