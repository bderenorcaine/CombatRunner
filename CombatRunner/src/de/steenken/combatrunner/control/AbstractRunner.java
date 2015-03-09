package de.steenken.combatrunner.control;

import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.CombatModel.NoMoreMovesException;

public abstract class AbstractRunner implements Runnable {
	
	private int combatRound = 0;
	private int combatPhase = 0;

	private final CombatModel combatants;
	
	AbstractRunner(CombatModel list) {
		this.combatants = list;
	}
	
	protected abstract void startCombat();
	
	protected void next() {
		try {
			combatants.next();
		} catch (NoMoreMovesException e) {
			for (Combatant c : combatants) {
				c.getInitiative().roll();
			}
		}
	}
	
	protected void addCombatant(final Combatant combatant) {
		combatants.addCombatant(combatant);
	}
	
}
