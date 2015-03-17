package de.steenken.combatrunner.model.initiative;

import de.steenken.combatrunner.dice.Dice.CriticalGlitch;
import de.steenken.combatrunner.dice.Dice.Glitch;
import de.steenken.combatrunner.dice.Dice.MultipleDiceException;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.EditionMisMatchError;
import de.steenken.combatrunner.model.CombatModel.Edition;

public class FourthEditionInitiative extends Initiative {

	private int initiativePasses = 1;
	
	private int lastRolled = 0;
	
	private boolean affectedByGlitch = false;
	
	private int actionsThisRound = 0;
	
	FourthEditionInitiative(Combatant combatant) {
		super(combatant);
	}
	
	FourthEditionInitiative(final Combatant combatant, final int passes) {
		super(combatant);
		initiativePasses = passes;
	}
	
	private int getValueThisPhase() {
		return getBaseValue() + lastRolled;
	}
	
	@Override
	public int compareTo(Initiative o) {
		if (o.getEdition() != getEdition()) {
			throw new EditionMisMatchError();
		} else {
			FourthEditionInitiative otherIni = (FourthEditionInitiative) o; 
			if (affectedByGlitch) {
				return -otherIni.getBaseValue() + otherIni.lastRolled;
			} else if (actionsThisRound < otherIni.actionsThisRound) {
				return Integer.MIN_VALUE;
			} else if (actionsThisRound > otherIni.actionsThisRound) {
				return Integer.MAX_VALUE;
			} else {
				return getValueThisPhase() - otherIni.getValueThisPhase();
			}
		}
	}

	@Override
	public void roll() {
		affectedByGlitch = false;
		actionsThisRound = 0;
		try {
			lastRolled = de.steenken.combatrunner.dice.Dice.getSuccesses(getBaseValue());
		} catch (Glitch e) {
			
		} catch (CriticalGlitch e) {
			affectedByGlitch = true;
		} catch (MultipleDiceException e) {
			throw new RuntimeException("Error - unknown MultipleDiceException " + e);
		}
	}

	@Override
	public void act() {
		actionsThisRound++;
	}

	@Override
	public boolean canAct() {
		if (initiativePasses > actionsThisRound) {
			return true;
		}
		return false;
	}

	@Override
	public Edition getEdition() {
		return Edition.FOURTH;
	}

	@Override
	public int getEditionAgnosticValue() {
		return initiativePasses;
	}

}
