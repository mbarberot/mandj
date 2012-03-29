package metaheuristics;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * 
 * Lastupdate : March 2011
 */

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Random;

import com.sun.xml.internal.bind.v2.model.annotation.Quick;

import problems.Problem;
import util.*;
import base.operators.crossover.*;
import base.operators.mutation.*;
import base.operators.selection.Selection;
import base.operators.selection.Tournament;
import base.variables.*;

public class AG_Simple {

	Problem problem_;
	private Genotype genotype;

	private Individual bestSolution;


	int popSize = 60;
	int nbGenerations = 100;

	Selection selection;
	Crossover crossover;
	Mutation mutation;

	float xProba = (float) 0.8;
	float mProba = (float) 0.01;


	public AG_Simple(Problem problem) {
		problem_ = problem;
		genotype = new Genotype(problem);

	} // Mprea

	/**   
	 * Runs of the MPREA algorithm.
	 * @return a set of non dominated solutions as a result of the algorithm execution  
	 * @throws ClassNotFoundException 
	 */  
	public Individual execute() throws ClassNotFoundException {

		List<Individual> popList = new ArrayList<Individual>();
		List<Individual> offspringsPop = new ArrayList<Individual>();

		for(int i = 0 ; i < popSize ; i++)
		{
			popList.add(new Individual(genotype));
		}
		
		Individual p1, p2;
		for(int i = 0 ; i < nbGenerations ; i++){
			for(int j = 0 ; j < popSize / 2 ; j++)
			{
				// SÃ©lection
				
				selection = new Tournament(popList);
				p1 = selection.doSelect();
				p2 = selection.doSelect();
				
				//Croisement des individus
				crossover = new OnePointCrossover(p1,p2);
				crossover.doCross(p1, p2);
				
				// Mutations				
				mutation = new UniformMutation(p1);
				mutation.doMutate();				
				mutation = new UniformMutation(p2);
				mutation.doMutate();
				
				// Evaluation des enfants
				problem_.evaluate(p1);
				problem_.evaluate(p2);
				
				offspringsPop.add(p1);
				offspringsPop.add(p2);
			}
			popList.clear();
			popList.addAll(offspringsPop);
			offspringsPop.clear();
		}
		
		bestSolution = findBest(popList);
		return bestSolution;
	} // execute


	/*
	 * 
	 */

	public Individual findBest(List<Individual> population){
		List<Individual>  sortedPopulation = QuickSort.sort(population);
			if (problem_.isTominize()){
				return sortedPopulation.get(0);
			} else {
				int lastIndex = sortedPopulation.size() - 1;
				return sortedPopulation.get(lastIndex);
			}
	}

	public Individual findBest(Individual indiv1, Individual indiv2){
		if (problem_.isTominize()){
			if(indiv1.getFitness() < indiv2.getFitness()){
				return indiv1;
			} else{
				return indiv2;
			}
		} else {
			if(indiv1.getFitness() > indiv2.getFitness()){
				return indiv1;
			} else{
				return indiv2;
			}
		} 
	}
	
	public Individual getBestSolution(){
		return bestSolution;
	}

}
