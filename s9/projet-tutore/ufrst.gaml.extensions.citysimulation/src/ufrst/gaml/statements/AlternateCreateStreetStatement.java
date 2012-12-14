package ufrst.gaml.statements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import javax.swing.Popup;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaList;
import msi.gama.util.GamaMap;
import msi.gaml.compilation.ISymbol;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.species.ISpecies;
import msi.gaml.statements.AbstractStatement;
import msi.gaml.statements.Arguments;
import msi.gaml.types.IType;

/**
 * Another CreateStreetStatement !
 * @author Mathieu Barberot et Joan Racenet
 *
 */
@symbol(name={"anotherCreateStreet"}, kind=ISymbolKind.SEQUENCE_STATEMENT, with_sequence=false)
@inside(kinds={ISymbolKind.BEHAVIOR, ISymbolKind.SEQUENCE_STATEMENT}) 
@facets(value={
		@facet(name = IKeyword.SPECIES, type = IType.SPECIES_STR, optional = false),
        @facet(name = IKeyword.RETURNS, type = IType.NEW_TEMP_ID, optional = false),
        @facet(name = IKeyword.FROM, type = IType.NONE_STR, optional = false),
        @facet(name = IKeyword.WITH, type = { IType.MAP_STR }, optional = false),
        }, 
        omissible = IKeyword.SPECIES)
public class AlternateCreateStreetStatement extends AbstractStatement 
{
	private Arguments args;
	private final IExpression species;
	private final IExpression returns;
	private final IExpression from;
	private final IExpression with;
	
	
	public AlternateCreateStreetStatement(IDescription desc) 
	{
		super(desc);
		
		System.out.println("<----- init ----- >");
		
		if(hasFacet(IKeyword.SPECIES))
		{
			species = getFacet(IKeyword.SPECIES);
			System.out.println("Facet --> Species = " + species);
		} else { species = null; }
		
		if(hasFacet(IKeyword.RETURNS))
		{
			returns = getFacet(IKeyword.RETURNS);
			System.out.println("Facet --> Returns = " + returns);
		} else { returns = null; }
		
		if(hasFacet(IKeyword.FROM))
		{
			from = getFacet(IKeyword.FROM);
			System.out.println("Facet --> From = " + from);
		} else { from = null; }
		
		if(hasFacet(IKeyword.WITH))
		{
			with = getFacet(IKeyword.WITH);
			System.out.println("Facet --> With = " + with);
		} else { with = null; }
		
		setName("anOtherCreateStreet");
		System.out.println("<----- /init ----- >");
		
	}

	@Override
	protected Object privateExecuteIn(IScope scope) throws GamaRuntimeException 
	{
		System.out.println("<----- executeIn ----->");
		
		final IAgent executor = scope.getAgentScope();
		final GamaList<IAgent> agents = new GamaList<IAgent>();
		
		
		IPopulation population = executor.getPopulationFor((ISpecies)species.value(scope));
		if(population == null)
		{
			System.out.println("Population --> null");
		}
		else 
		{
			System.out.println("Population --> " + population.getAgentsList());
		
			population.createAgents(scope, 1, new GamaList(), false);
			System.out.println("createAgents --> " + population.getAgentsList());
		}
		System.out.println("<----- /executeIn ----->");
		return agents;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
    public void setChildren(final List<? extends ISymbol> commands)
	{
		System.out.println("setChildren --> " + commands);
		super.setChildren(commands);
	}
}