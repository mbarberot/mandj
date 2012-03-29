package base.operators.selection;

/**
 * Tournament Selection based on the C-language implementation of SGA by R.
 * Smith, D. Goldberg and J. Earickson.
 */

import java.util.*;

import base.variables.*;
public class Tournament extends Selection{

	private int[] tourneylist;

	private int tourneypos;

	private int tourneysize;


	/**
	 * @param 
	 * 
	 */

	public Tournament(List<Individual> _pop) {
		super(_pop);
		tourneylist = new int[population.size()];
		tourneysize = 10;
		preselect();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see operators.Selector#preselect()
	 */
	public void preselect() {
		shuffleList();
		tourneypos = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see operators.Selector#select()
	 */
	public Individual doSelect() {
		
		int choice, winner = 0;
		
		// On lance les duels
		for(int i = 0 ; i <= tourneysize ; i++)
		{
			Individual current = population.get(i);
			
			for(int j = i + 1 ; j <= tourneysize ; j++)
			{
				double scoreCu, scoreAdv;
				
				Individual adverse = population.get(j);
				
				// R�cup�ration du score des deux adversaires (Fitness(I) * Alea(I))
				scoreCu = current.getFitness() * (Math.random() + 0.01);
				scoreAdv = adverse.getFitness() * (Math.random() + 0.01);
				
				// On compare les scores et on en d�duit le vainqueur
				if(scoreCu >= scoreAdv)
				{
					tourneylist[i]++;
				}
				else
				{
					tourneylist[j]++;
				}
				
			}
		}
		
		// Trouve le vainqueur
		winner = tourneylist[0];
		for(int i = 0 ; i < tourneylist.length ; i++)
		{
			if(tourneylist[i] > winner)
				winner = tourneylist[i];
		}

		return population.get(winner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see operators.Selector#reset()
	 */
	public void reset() {

	}

	/*
	 * 
	 */
	private void shuffleList() {
		Collections.shuffle(population);
	}

}
