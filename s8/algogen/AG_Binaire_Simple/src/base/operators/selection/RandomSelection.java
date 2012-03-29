package base.operators.selection;

import java.util.List;

import base.variables.Individual;

public class RandomSelection extends Selection{

	public RandomSelection(List<Individual> population) {
		super(population);
	}

	@Override
	public Individual doSelect() {
		
		int index = (int) (Math.random()*population.size());
		
		return population.get(index);
	}

}
