package de.steenken.combatrunner.model.initiative;

import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.EditionMisMatchError;
import de.steenken.combatrunner.model.CombatModel.Edition;

public class FifthEditionInitiative extends Initiative {

	private int initiativeDice = 1;
	
	private int lastRoll = 0;
	
	public FifthEditionInitiative(final Combatant combatant) {
		super(combatant);
	}
	
	public FifthEditionInitiative(final Combatant combatant, final int dice) {
		super(combatant);
		initiativeDice = dice;
	}

	@Override
	public int compareTo(Initiative o) {
		if (o.getEdition() != getEdition()) {
			throw new EditionMisMatchError();
		} else {
			return lastRoll - ((FifthEditionInitiative) o).lastRoll;
		}
	}

	@Override
	public void roll() {
		lastRoll = de.steenken.combatrunner.dice.Dice.rollDiceForSum(initiativeDice);
	}

	@Override
	public void act() {
		lastRoll -= 10;
	}

	@Override
	public boolean canAct() {
		return (getBaseValue() + lastRoll > 0);
	}

	@Override
	public Edition getEdition() {
		return Edition.FIFTH;
	}
	
	public final String toString() {
		return getBaseValue() + " + " + initiativeDice + "D6";
	}

	@Override
	public int getEditionAgnosticValue() {
		return initiativeDice;
	}

}
