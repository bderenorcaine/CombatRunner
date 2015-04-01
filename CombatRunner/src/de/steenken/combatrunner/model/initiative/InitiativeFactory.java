package de.steenken.combatrunner.model.initiative;

import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.Combatant;

public abstract class InitiativeFactory {

	public static InitiativeFactory getFourthEditionFactory() {
		return new FourthEditionInitiativeFactory();
	}

	public static InitiativeFactory getFifthEditionFactory() {
		return new FifthEditionInitiativeFactory();
	}

	public abstract Initiative makeDefaultInitiative(final Combatant combatant);
	
	public abstract Initiative makeInitiative(final Combatant combatant, final int number, final int bonus);
	
	public abstract CombatModel.Edition getEdition();
}
