package de.steenken.combatrunner.model;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractListModel;

import de.steenken.combatrunner.model.initiative.InitiativeComparator;
import de.steenken.combatrunner.model.initiative.InitiativeFactory;

/**
 * @author dominik
 * 
 */
public class CombatModel extends AbstractListModel<Combatant> implements Iterable<Combatant> {

	public enum Edition {
		FOURTH, FIFTH;
	}
	
	private final Edition edition;
	
	private String name = "Unnamed Combat";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CombatModel(final Edition edition) {
		this.edition = edition;
	}
	
	public final class NoMoreMovesException extends Exception {
	}
	
	private final LinkedList<Combatant> combatants = new LinkedList<>();
	private final InitiativeComparator comparator = new InitiativeComparator();

	public void next() throws NoMoreMovesException {
		combatants.getFirst().getInitiative().act();
		reorderAndNotify();
		if (!combatants.getFirst().canAct()) {
			throw new NoMoreMovesException();
		}
	}
	
	@Override
	public Iterator<Combatant> iterator() {
		return new ConstListIterator<>(combatants.listIterator());
	}

	private void reorderAndNotify() {
		Collections.sort(combatants, comparator);
		fireContentsChanged(this, 0, getSize());
	}

	@Override
	public Combatant getElementAt(int arg0) {
		try {
			return combatants.get(arg0);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public int getSize() {
		return combatants.size();
	}

	public void addCombatant(final Combatant combatant) {
		combatants.add(combatant);
		reorderAndNotify();
	}

	public InitiativeFactory getInitiativeFactory() {
		if (edition == Edition.FOURTH) {
			return InitiativeFactory.getFourthEditionFactory();
		} else {
			return InitiativeFactory.getFifthEditionFactory();
		}
	}
	
	public final Edition getEdition() {
		return edition;
	}
}
