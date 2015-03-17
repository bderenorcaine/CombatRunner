package de.steenken.combatrunner.model.initiative;

import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.Combatant.CombatantBuilder;
import de.steenken.combatrunner.model.attributes.Attribute.Name;

public abstract class Initiative implements Comparable<Initiative>{

	protected Combatant combatant;
	
	Initiative(Combatant combatant) {
		this.combatant = combatant;
	}
	
	public abstract void roll();
	public abstract void act();
	public abstract boolean canAct();
	public abstract CombatModel.Edition getEdition();
	public void attach(CombatantBuilder builder) {
		if (combatant == builder) {
			combatant = builder.attach(this);
		}
	}

	protected int getBaseValue() {
		return combatant.getAttribute(Name.REA) + combatant.getAttribute(Name.INT);
	}
	
	public abstract int getEditionAgnosticValue();
}
