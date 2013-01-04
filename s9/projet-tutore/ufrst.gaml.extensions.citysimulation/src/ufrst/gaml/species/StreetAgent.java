package ufrst.gaml.species;

import com.vividsolutions.jts.geom.Geometry;

import msi.gama.kernel.simulation.ISimulation;
import msi.gama.metamodel.agent.GamlAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.metamodel.shape.GamaPoint;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.metamodel.shape.IShape;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.setter;
import msi.gama.precompiler.GamlAnnotations.species;
import msi.gama.precompiler.GamlAnnotations.var;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaList;
import msi.gama.util.IList;
import msi.gaml.types.IType;

/**
 * Agent for streets. <br/> 
 * Streets can be one-way (oriented edge of a graph) or bi-directionnal.
 *  
 * @author Barberot Mathieu
 * @author Joan Racenet
 *
 */
@species(name="Street")
@vars({
	@var(name = "waytype", type = IType.STRING_STR), 
	@var(name = "idstart", type = IType.INT_STR, init = "-1"),
	@var(name = "idend", type = IType.INT_STR, init = "-1"),
	@var(name = "clone", type = IType.BOOL_STR, init = "false")
	})
public class StreetAgent extends GamlAgent
{        
        /**
         * Flag for one-way streets
         * True if this is a one-way streets, false otherwise
         */
        private boolean oneWay = false;

        /** Identifier of the node of the beginning of the street (used if the street is a one-way)*/
        private int idStart;
        
        /** Identifier of the node of the end of the street (used if the street is a one-way)*/
        private int idEnd;
        
        //TODO : à virer id start et id end
        private boolean isClone = false;
        
        public StreetAgent(ISimulation sim, IPopulation s) throws GamaRuntimeException 
        {
                super(sim, s);
        }

        /**
         * GETTERS & SETTERS used in GAML
         */
        @setter("waytype")
        public void setWayType(String isOneWay)
        {
        	//TODO => comparaison sur 0 : double sens / 1 : sens unique / 2 : sens unique avec inversion du sens de digitalisation
        	oneWay = (isOneWay.equalsIgnoreCase("UNIQUE"));
        	setAttribute("waytype", isOneWay);
        	
        	// If not a one way => adding a copy of the agent with an inversed geometry in the population
        	if(!oneWay)
        	{
        		StreetAgent theCopy = new StreetAgent(getSimulation(), getPopulation());
        		Geometry geom = ((Geometry) this.getInnerGeometry().clone()).reverse(); 
        	
        		theCopy.setGeometry(this.getGeometry());
        		theCopy.setInnerGeometry(geom);
        		
        		// Display the state of the geometry
        		System.out.println("Geometry of this : " + this.getInnerGeometry().toString());
        		System.out.println("Geometry of the clone : " + theCopy.getInnerGeometry().toString());
        		theCopy.setLocation(getLocation());        		
        		      		
        		
        		System.out.println("Number of roads in the population : " + getPopulation().size());
        		//Add the new agent in the population
        		theCopy.setClone(true);
        		getPopulation().add(theCopy, null);
        		System.out.println("Number of roads in the population (after clone) : " + getPopulation().size());
        	}
        		
        }
              
        public void setWayType(boolean isOneWay)
        {
        	this.oneWay = isOneWay;
        }
        
        @getter("waytype")
        public String getWayType()
        {
        	return (oneWay)? "UNIQUE" : "DOUBLE";
        }
        
        @setter("idstart")
        public void setIdStart(int start)
        {
        	idStart = start;   
        	this.setAttribute("idstart", start);
        }
        
        @getter("idstart")
        public int getIdStart()
        {
        	return idStart;
        }
        
        @setter("idend")
        public void setIdEnd(int end)
        {
        	idEnd = end;
        	this.setAttribute("idend", end);
        }
        
        @getter("idend")
        public int getIdEnd()
        {
        	return idEnd;
        }
        
        @setter("clone")
        public void setClone(boolean isAClone)
        {
        	isClone = isAClone;
        	this.setAttribute("clone", isAClone);
        }
        
        @getter("clone")
        public boolean setClone()
        {
        	return isClone;
        }


}