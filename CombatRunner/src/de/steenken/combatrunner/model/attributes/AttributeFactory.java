package de.steenken.combatrunner.model.attributes;

import de.steenken.combatrunner.model.attributes.RaceDefaults.Race;

public class AttributeFactory {

	private AttributeFactory() {
		
	}
	
	public static AttributeArray makeStandardSpecimen(Race race) {
		return RaceDefaults.getDefaults(race);
	}
	
}
