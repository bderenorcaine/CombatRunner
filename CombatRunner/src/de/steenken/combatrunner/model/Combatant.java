package de.steenken.combatrunner.model;

import de.steenken.combatrunner.model.attributes.Attribute;
import de.steenken.combatrunner.model.attributes.AttributeArray;
import de.steenken.combatrunner.model.attributes.AttributeFactory;
import de.steenken.combatrunner.model.attributes.Attribute.Name;
import de.steenken.combatrunner.model.initiative.Initiative;

public class Combatant {

	private AttributeArray attributes = null;

	private Initiative initiative = null;

	private String name = null;

	private final CombatModel combat;

	private Combatant(final CombatModel combat) {
		this.combat = combat;
	}

	public final Initiative getInitiative() {
		return initiative;
	}

	public final String getName() {
		return name;
	}

	public final int getAttribute(Name name) {
		return attributes.getValue(name, 0);
	}

	public final boolean canAct() {
		return initiative.canAct();
	}

	public static CombatantBuilder newBuilder(final CombatModel combat) {
		return new CombatantBuilder(combat);
	}

	public static class CombatantBuilder extends Combatant {

		private final Combatant combatant;

		private CombatantBuilder(final CombatModel combat) {
			super(combat);
			combatant = new Combatant(combat);
		}

		public CombatantBuilder setName(final String name) {
			combatant.name = name;
			return this;
		}

		public CombatantBuilder setAttribute(Attribute.Name name, int value) {
			if (combatant.attributes == null) {
				combatant.attributes = new AttributeArray();
			}
			combatant.attributes.setValue(name, value);
			return this;
		}
		
		public CombatantBuilder setAttributes(final AttributeArray attributes) {
			combatant.attributes = attributes;
			return this;
		}

		public CombatantBuilder setInitiative(Initiative initiative)
				throws EditionMisMatchError {
			if (initiative.getEdition() != combatant.combat.getEdition()) {
				throw new EditionMisMatchError();
			}
			combatant.initiative = initiative;
			return this;
		}

		public Combatant build() throws IncompleteModelException {
			if ((combatant.attributes != null)
					&& combatant.attributes.minComplete()
					&& (combatant.initiative != null)
					&& (combatant.name != null)) {
				combatant.initiative.attach(this);
				return combatant;
			} else {
				throw new IncompleteModelException();
			}
		}

		public Combatant attach(Initiative initiative) {
			return combatant;
		}
	}

	public final String toString() {
		return name;
	}
	
	public final String fullDescription() {
		String result = "Character: " + name + "\n";
		result += attributes.toString();
		result += "Initiative: " + initiative.toString();
		return result;
	}
}
