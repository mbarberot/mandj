package ufrst.gaml.skills;

import msi.gama.precompiler.GamlAnnotations.action;
import msi.gama.precompiler.GamlAnnotations.arg;
import msi.gama.precompiler.GamlAnnotations.skill;
import msi.gama.runtime.IScope;
import msi.gama.runtime.exceptions.GamaRuntimeException;
import msi.gaml.skills.MovingSkill;
import msi.gaml.types.IType;


@skill(name = "citymoving")
public class CityMovingSkill extends MovingSkill 
{
	@action(name="driveto", args={
			@arg(name="target", type="service", optional=false),
			@arg(name="on", type=IType.GRAPH_STR)
	})
	public boolean driveTo(final IScope scope) throws GamaRuntimeException
	{
		return true;
	}
}

