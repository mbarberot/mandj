package ufrst.gaml.species;

import org.geotools.styling.Description;

import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.CoordinateSequenceComparator;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.GeometryFilter;
import com.vividsolutions.jts.geom.LineSegment;

import msi.gama.kernel.simulation.ISimulation;
import msi.gama.metamodel.agent.GamlAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.metamodel.shape.IShape;
import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.species;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaPath;
import msi.gaml.descriptions.IDescription;
import msi.gaml.factories.DescriptionFactory;
import msi.gaml.species.GamlSpecies;
import msi.gaml.statements.AspectStatement;
import msi.gaml.statements.IStatement;

/**
 * Agent for streets. <br/> 
 * Streets can be one-way (oriented edge of a graph) or bi-directionnal.
 *  
 * @author Barberot Mathieu
 * @author Joan Racenet
 *
 */
@species(name="Street")
public class AgentStreet extends GamlAgent
{
	
	/**
	 * Flag for one-way streets
	 * True if this is a one-way streets, false otherwise
	 */
	private boolean oneWay;

	
	public AgentStreet(ISimulation sim, IPopulation s) throws GamaRuntimeException 
	{
		super(sim, s);
	}







	/**
	 * Is it a one-way street ?
	 * 
	 * @param scope
	 * @return True is this is a one-way streets, false otherwise
	 * @throws GamaRuntimeException
	 */
	@action(name="isOneWay")
	public boolean isOneWay(final IScope scope) throws GamaRuntimeException {
		return oneWay;
	}


}
