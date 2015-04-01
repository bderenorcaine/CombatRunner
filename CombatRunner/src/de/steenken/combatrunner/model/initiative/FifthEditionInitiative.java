package de.steenken.combatrunner.model.initiative;

import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.EditionMisMatchError;
import de.steenken.combatrunner.model.CombatModel.Edition;

public class FifthEditionInitiative extends Initiative {

	private int initiativeDice = 1;
	
	private int initiativeBonus = 0;
	
	private int lastRoll = 0;
	
	public FifthEditionInitiative(final Combatant combatant) {
		super(combatant);
	}
	
	public FifthEditionInitiative(final Combatant combatant, final int dice, final int bonus) {
		super(combatant);
		initiativeDice = dice;
		initiativeBonus = bonus;
	}

	@Override
	public int compareTo(Initiative o) {
		if (o.getEdition() != getEdition()) {
			throw new EditionMisMatchError();
		} else {
			return lastRoll - ((FifthEditionInitiative) o).lastRoll;
		}
	}

	private int getCurrentValue() {
		return getBaseValue() + lastRoll + initiativeBonus;
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
		return (getCurrentValue() > 0);
	}

	@Override
	public Edition getEdition() {
		return Edition.FIFTH;
	}
	
	public final String toString() {
		return getBaseValue() + " + " + initiativeBonus + " + " + initiativeDice + "D6";
	}

	@Override
	public int getEditionAgnosticValue() {
		return initiativeDice;
	}
	
	@Override
	public int getEditionAgnosticBonus() {
		return initiativeBonus;
	}

}
