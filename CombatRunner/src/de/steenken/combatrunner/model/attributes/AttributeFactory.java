package de.steenken.combatrunner.model.attributes;

import de.steenken.combatrunner.model.attributes.MetatypeDefaults.Metatype;

public class AttributeFactory {

	private AttributeFactory() {
		
	}
	
	public static AttributeArray makeStandardSpecimen(Metatype race) {
		return MetatypeDefaults.getDefaults(race);
	}
	
}
