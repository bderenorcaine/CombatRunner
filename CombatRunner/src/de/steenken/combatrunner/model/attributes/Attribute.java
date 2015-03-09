package de.steenken.combatrunner.model.attributes;

import de.steenken.combatrunner.conf.GlobalConstants;

public class Attribute {

	public enum Name {
		STR("Strength", "Stärke"), BOD("Body", "Konstitution"), AGI("Agility",
				"Geschicklichkeit"), REA("Reaction", "Reaktion"), INT(
				"Intuition", "Intuition"), LOG("Logic", "Logik"), CHA(
				"Charisma", "Charisma"), WIL("Willpower", "Willenskraft"), EDG(
				"Edge", "Edge"), MAG("Magic", "Magie"), RES("Resonance",
				"Resonanz");

		private final String name;
		private final String germanName;

		private Name(String name, String germanName) {
			this.name = name;
			this.germanName = germanName;
		}

		public final String toString() {
			switch (GlobalConstants.language) {
			case ENGLISH:
				return name;
			case GERMAN:
				return germanName;
			default:
				return name + "_" + GlobalConstants.language + "?";
			}
		}
	}

	private int value;
	
	Attribute(final int value) {
		this.value = value;
	}
	
	public final int getValue() {
		return value;
	}
	
	final void setValue(final int value) {
		this.value = value;
	}
}
