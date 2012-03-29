package metaheuristics;

import java.util.ArrayList;
import java.util.List;

import problems.*;

import base.variables.*;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * 
 * Lastupdate : March 2011
 */

public class AG_Simple_Main {

	public static void main(String[] args) {
		Individual bestSolution = null;	

		Problem problem = new TroisCercles();
		
		AG_Simple algo = new AG_Simple(problem);
		try {
			bestSolution = algo.execute();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("La meilleure valeur de fitness trouvee est : " + bestSolution.getFitness());
	}

}
