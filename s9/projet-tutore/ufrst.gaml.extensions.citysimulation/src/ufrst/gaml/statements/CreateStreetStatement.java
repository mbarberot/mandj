package ufrst.gaml.statements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;

import msi.gama.common.interfaces.IKeyword;
import msi.gama.common.util.GisUtils;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.metamodel.population.IPopulation;
import msi.gama.metamodel.shape.GamaShape;
import msi.gama.precompiler.GamlAnnotations.facet;
import msi.gama.precompiler.GamlAnnotations.facets;
import msi.gama.precompiler.GamlAnnotations.inside;
import msi.gama.precompiler.GamlAnnotations.symbol;
import msi.gama.precompiler.ISymbolKind;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gama.util.GamaList;
import msi.gama.util.GamaMap;
import msi.gama.util.file.GamaFile;
import msi.gaml.compilation.AbstractGamlAdditions;
import msi.gaml.descriptions.IDescription;
import msi.gaml.expressions.IExpression;
import msi.gaml.operators.Cast;
import msi.gaml.species.ISpecies;
import msi.gaml.statements.AbstractStatementSequence;
import msi.gaml.statements.Arguments;
import msi.gaml.statements.CreateStatement;
import msi.gaml.statements.Facets.Facet;
import msi.gaml.statements.IStatement;
import msi.gaml.types.GamaFileType;
import msi.gaml.types.IType;

/**
 * Custom statement for streets creation.<br/>
 * This statement must be used with a GIS file. Every street is a one-way, as an edge of an oriented graph. Streets wich have two or more ways are doubled.
 * 
 * 
 * @author Barberot Mathieu and Racenet Joan
 * @author (Referee : Marilleau Nicolas and Lang Christophe)
 */
@symbol(name="createStreet", kind=ISymbolKind.SEQUENCE_STATEMENT, with_sequence = true, with_args = true, remote_context = true)
@inside(kinds={ISymbolKind.SEQUENCE_STATEMENT,ISymbolKind.BEHAVIOR})
@facets(value={
		@facet(name=IKeyword.SPECIES, type=IType.SPECIES_STR, optional=true),
		@facet(name=IKeyword.FROM, type=IType.NONE_STR, optional=false),
		@facet(name=IKeyword.RETURNS, type=IType.NEW_TEMP_ID, optional=false),
		@facet(name=IKeyword.WITH, type=IType.MAP_STR, optional=false)
}, omissible=IKeyword.SPECIES)
public class CreateStreetStatement extends AbstractStatementSequence implements IStatement.WithArgs
{
	private Arguments init;
	private IExpression from;
	private IExpression speciesExpr;
	private String returnString;
	private MathTransform transformCRS = null;
	private AbstractStatementSequence sequence;
	
	/**
	 * Constructor
	 * Init with arguments from the facets
	 * @param desc 
	 */
	public CreateStreetStatement(IDescription desc) {
		super(desc);
		returnString = getLiteral(IKeyword.RETURNS);
		from = getFacet(IKeyword.FROM);
		speciesExpr = getFacet(IKeyword.SPECIES);
		setName("createStreet");
	}
	
	/**
	 * Constructor-bis
	 * Init with "null" values
	 */
	public CreateStreetStatement()
	{
		super(null);
		returnString = null;
		from = null;
		speciesExpr = null;
	}

	@Override
	public void setFormalArgs(Arguments args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRuntimeArgs(Arguments args) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object privateExecuteIn(final IScope scope) throws GamaRuntimeException
	{
		final IAgent executor = scope.getAgentScope();
		
		if(from == null )
		{
			return new GamaList();
		}
		
		final GamaList<IAgent> agents = new GamaList<IAgent>();
		
		
		IPopulation thePopulation;
		if(speciesExpr == null)
		{
			thePopulation = executor.getPopulationFor(description.getSpeciesContext().getName());
		}
		else
		{
			ISpecies targetSpecies = (ISpecies) speciesExpr.value(scope);
			if(targetSpecies == null)
			{
				String availableSpecies =  accumulateAvailableSpecs(executor);
				throw new GamaRuntimeException("Population of " + speciesExpr.literalValue() + 
						" species is not accessible from the context of " + executor.getName() + 
						" agent. Available populations are [" + availableSpecies + "] ");
			}
			
			thePopulation = executor.getPopulationFor(targetSpecies);
		}
		
		scope.addVarWithValue(IKeyword.MYSELF, scope.getAgentScope());
		
		if(from != null)
		{
			IType type = from.getType();
			
			if(type.id() == IType.STRING || type.id() == IType.FILE)
			{
				String fileStr = "";
				try
				{
					Object file = from.value(scope);
					fileStr = (file instanceof String) ?
								scope.getSimulationScope().getModel().getRelativeFilePath(Cast.asString(scope, file), true) :
								((GamaFile)file).getPath();
					if(GamaFileType.isShape(fileStr))
					{
						createAgentsFromGIS(scope,agents, thePopulation);
					}
				}
				catch (GamaRuntimeException ex)
				{
					ex.printStackTrace();
				}
			}
			else
			{
				return new GamaList();
			}
		}
		
		return null;
	}
	
	private void createAgentsFromGIS(final IScope scope, final GamaList<IAgent> agents, IPopulation thePopulation) 
	{
		FeatureIterator<SimpleFeature> it3 = getFeatureIterator(scope);
		final List<Map<String, Object>> initialValues = new GamaList();
		
		if(it3 != null)
		{
			int index = 0;
			int max = Integer.MAX_VALUE;
			while(it3.hasNext() && index <= max)
			{
				SimpleFeature fact = it3.next();
				Geometry geom = (Geometry) fact .getDefaultGeometry();
				if(geom == null)
				{
					continue;
				}
				index++;
				GisUtils.setCurrentGisReader(fact);
				
				if(transformCRS != null)
				{
					try 
					{
						geom = JTS.transform(geom, transformCRS);
					}
					catch (MismatchedDimensionException ex1)
					{
						ex1.printStackTrace();
					}
					catch (TransformException ex2)
					{
						ex2.printStackTrace();
					}
				}
				
				geom = GisUtils.fromAbsoluteToGis(geom);
				
				Map<String,Object> map = new GamaMap();
				computeInits(scope,map);
				map.put(IKeyword.SHAPE, new GamaShape(geom));
				initialValues.add(map);
			}
			
			it3.close();
			createAgents(scope, agents, thePopulation, initialValues, index);
            GisUtils.setCurrentGisReader(null);
		}
		
		
	}

	/**
	 * 
	 * 
	 * @param scope
	 * @param values
	 * @throws GamaRuntimeException
	 */
	private void computeInits(final IScope scope, final Map<String, Object> values) throws GamaRuntimeException 
	{
		if(init == null)
		{
			return;
		}
		
		for(Facet f : init.entrySet())
		{
			if(f == null)
			{
				IExpression valueExpr = f.getValue().getExpression();
				Object val = valueExpr.value(scope);
				values.put(f.getKey(), val);
			}
		}
		System.out.println("----------<computeInits>--------------");
		System.out.println(values.toString());
		System.out.println("----------</computeInits>--------------");
	}

	/**
	 * Création des agents
	 * 
	 * @param scope 		Scope
	 * @param agents 		Liste des agents
	 * @param population 	Population
	 * @param initialValues Valeurs initiales
	 * @param number 		Nombre d'agents
	 */
	private void createAgents(final IScope scope, final GamaList<IAgent> agents, final IPopulation population, final List<Map<String, Object>> initialValues, int number) 
	{
		if(number == 0)
		{
			return;
		}
		
		List<? extends IAgent> list = population.createAgents(scope, number, initialValues, false);
		
		if(!sequence.isEmpty())
		{
			for(int i = 0; i < number; i++)
			{
				IAgent remoteAgent = list.get(i);
				scope.execute(sequence, remoteAgent);
			}
		}
		agents.addAll(list);
			
	}

	private FeatureIterator<SimpleFeature> getFeatureIterator(final String shapeFile) 
	{
		try 
		{
			File shpFile = new File(shapeFile);
			ShapefileDataStore store = new ShapefileDataStore(shpFile.toURI().toURL());
			String name = store.getTypeNames()[0];
			FeatureSource<SimpleFeatureType, SimpleFeature> source = store.getFeatureSource(name);
			FeatureCollection<SimpleFeatureType, SimpleFeature> featureShp = source.getFeatures();
			if (store.getSchema().getCoordinateReferenceSystem() != null) 
			{
				if (GisUtils.transformCRS == null) 
				{
					ShpFiles shpf = new ShpFiles(shpFile);
					double latitude = featureShp.getBounds().centre().x;
					double longitude = featureShp.getBounds().centre().y;
					transformCRS = GisUtils.getTransformCRS(shpf, latitude,longitude);
				} 
				else 
				{
					transformCRS = GisUtils.transformCRS;
				}
			}
			return featureShp.features();
			
		} 
		catch (IOException e) 
		{
			return null;
		}
	}

	private FeatureIterator<SimpleFeature> getFeatureIterator(final IScope scope) 
	{
		String shapeFile = "";
		try 
		{
			Object shfile = from.value(scope);
			shapeFile = (shfile instanceof String) ? 
						scope.getSimulationScope().getModel().getRelativeFilePath(Cast.asString(scope, shfile), true) :
						((GamaFile) shfile).getPath();
		} 
		catch (GamaRuntimeException e) 
		{
			e.printStackTrace();
		}
		
		return getFeatureIterator(shapeFile);
	}

	private String accumulateAvailableSpecs(final IAgent executor) 
	{
		StringBuffer retVal = new StringBuffer();
		boolean firstSpec = false;
		ISpecies currentSpec = executor.getSpecies();
		for ( ISpecies m : currentSpec.getMicroSpecies() ) 
		{
			if ( AbstractGamlAdditions.isBuiltIn(m.getName()) ) 
			{
				continue;
			}

			if ( !firstSpec ) 
			{
				firstSpec = true;
				retVal.append(m.getName());
			}
			else 
			{
				retVal.append(", " + m.getName());
			}
		}

		while (!currentSpec.getName().equals(IKeyword.WORLD_SPECIES)) 
		{
			if ( !AbstractGamlAdditions.isBuiltIn(currentSpec.getName()) ) 
			{
				if ( !firstSpec ) 
				{
					firstSpec = true;
					retVal.append(currentSpec.getName());
				} 
				else 
				{
					retVal.append(", " + currentSpec.getName());
				}
			}

			for ( ISpecies p : currentSpec.getPeersSpecies() ) 
			{
				if ( AbstractGamlAdditions.isBuiltIn(p.getName()) ) 
				{
					continue;
				}
				retVal.append(", " + p.getName());
			}

			currentSpec = currentSpec.getMacroSpecies();
		}

		return retVal.toString();
	}
	

}
