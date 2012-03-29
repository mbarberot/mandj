package base.operators.selection;

import java.util.ArrayList;
import java.util.List;
import base.variables.*;

/**
 * @author Wahabou Abdou
 * wahabou.abdou@univ-fcomte.fr
 *
 * 
 * June 2010
 * 
 * Lastupdate : March 2011
 */

public abstract class Selection {
	
	List<Individual> population;
	
	public Selection (List<Individual> population){
		this.population = population;
	}
	
	public abstract Individual doSelect();

}