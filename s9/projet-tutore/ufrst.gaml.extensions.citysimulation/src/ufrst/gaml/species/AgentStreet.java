package ufrst.gaml.species;

import msi.gama.kernel.simulation.ISimulation;
import msi.gama.metamodel.agent.GamlAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.precompiler.GamlAnnotations.getter;
import msi.gama.precompiler.GamlAnnotations.setter;
import msi.gama.precompiler.GamlAnnotations.species;
import msi.gama.precompiler.GamlAnnotations.var;
import msi.gama.precompiler.GamlAnnotations.vars;
import msi.gama.runtime.exceptions.GamaRuntimeException;
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
	@var(name = "idend", type = IType.INT_STR, init = "-1")
	})
public class AgentStreet extends GamlAgent
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
        
        public AgentStreet(ISimulation sim, IPopulation s) throws GamaRuntimeException 
        {
                super(sim, s);
        }

        /**
         * GETTERS & SETTERS used in GAML
         */
        @setter("waytype")
        public void setWayType(String isOneWay)
        {
        	//TODO [DEBUG]
        	System.out.println("Set my way type ");
        	System.out.println("----------\n"+ isOneWay);
        	oneWay = (isOneWay.equalsIgnoreCase("UNIQUE"));
        	System.out.println("waytype = " + oneWay + "\n---------------");
        }
                
        @getter("waytype")
        public String getWayType()
        {
        	//TODO [DEBUG]
        	System.out.println("Get my way type ");
        	
        	
        	/*if(this.getAttribute("waytype") == null)
        	{
        		System.out.println("GetWayType Attribute == null");
        		return "DOUBLE";
        	}*/
        		
        	System.out.println("waytype =" + oneWay );
        	return (oneWay)? "UNIQUE" : "DOUBLE";
        }
        
        @setter("idstart")
        public void setIdStart(int start)
        {
        	//TODO [DEBUG]
        	System.out.println("Set my idStart with : " + start);
        	idStart = start;   
        	this.setAttribute("idstart", start);
        }
        
        @getter("idstart")
        public int getIdStart()
        {
        	//TODO [DEBUG]
        	System.out.println("Get my idStart");
        	return idStart;
        }
        
        @setter("idend")
        public void setIdEnd(int end)
        {
        	//TODO [DEBUG]
        	System.out.println("Set my idEnd with : " + end);
        	idEnd = end;
        	this.setAttribute("idend", end);
        }
        
        @getter("idend")
        public int getIdEnd()
        {
        	//TODO [DEBUG]
        	System.out.println("Get my idEnd");
        	return idEnd;
        }


}