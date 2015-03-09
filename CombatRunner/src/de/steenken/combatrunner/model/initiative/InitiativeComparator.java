package de.steenken.combatrunner.model.initiative;

import java.util.Comparator;

import de.steenken.combatrunner.model.Combatant;

public class InitiativeComparator implements Comparator<Combatant> {

	@Override
	public int compare(Combatant arg0, Combatant arg1) {
		return arg0.getInitiative().compareTo(arg1.getInitiative());
	}
	
}
