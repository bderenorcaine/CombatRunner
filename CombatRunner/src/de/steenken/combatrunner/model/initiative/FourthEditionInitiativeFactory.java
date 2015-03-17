package de.steenken.combatrunner.model.initiative;

import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.CombatModel.Edition;

public class FourthEditionInitiativeFactory extends InitiativeFactory {

	@Override
	public Initiative makeDefaultInitiative(Combatant combatant) {
		return new FourthEditionInitiative(combatant);
	}

	@Override
	public Edition getEdition() {
		return Edition.FOURTH;
	}

	@Override
	public Initiative makeInitiative(Combatant combatant, int number) {
		return new FourthEditionInitiative(combatant, number);
	}

}
