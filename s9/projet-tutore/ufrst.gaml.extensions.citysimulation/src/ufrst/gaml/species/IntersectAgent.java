package ufrst.gaml.species;

import msi.gama.kernel.simulation.ISimulation;
import msi.gama.metamodel.agent.GamlAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.precompiler.GamlAnnotations.species;
import msi.gama.precompiler.GamlAnnotations.var;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.setter;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.types.IType;

/**;
 * Agent for intersections. <br/>
 *  
 * @author Barberot Mathieu
 * @author Joan Racenet
 *
 */

@species(name = "Intersection")
@vars({@var(name = "identifier", type = IType.INT_STR) })
public class IntersectAgent extends GamlAgent {

	/** The identifier of the intersection */
	private int id;
	
	public IntersectAgent(ISimulation sim, IPopulation s)
			throws GamaRuntimeException {
		super(sim, s);
	}
	
	/**
	 * Getters & setters used in GAML
	 */
	@setter("identifier")
	public void setId(int id)
	{
		//TODO [DEBUG]
    	System.out.println("Set my id ");
    	this.id = id;
    	this.setAttribute("identifier", id);
	}
	
	@getter("identifier")
    public int getIdStart()
    {
    	//TODO [DEBUG]
    	System.out.println("Get my id");
    	return this.id;
    }
	
}
