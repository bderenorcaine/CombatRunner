package de.steenken.combatrunner.test;

import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.CombatModel.Edition;
import de.steenken.combatrunner.model.Combatant.CombatantBuilder;
import de.steenken.combatrunner.model.attributes.AttributeFactory;
import de.steenken.combatrunner.model.attributes.Attribute.Name;
import de.steenken.combatrunner.model.attributes.RaceDefaults.Race;
import de.steenken.combatrunner.model.initiative.InitiativeFactory;

public class ThrowawayMain {

	public static void main(String[] args) {
		CombatModel combat = new CombatModel(Edition.FIFTH);
		CombatantBuilder builder = Combatant.newBuilder(combat);
		InitiativeFactory iniFactory = InitiativeFactory
				.getFifthEditionFactory();
		try {
			Combatant ono = builder.setName("Ono").setAttribute(Name.BOD, 8)
					.setAttribute(Name.AGI, 5).setAttribute(Name.REA, 7)
					.setAttribute(Name.STR, 5).setAttribute(Name.CHA, 3)
					.setAttribute(Name.INT, 4).setAttribute(Name.LOG, 2)
					.setAttribute(Name.WIL, 3).setAttribute(Name.EDG, 3)
					.setAttribute(Name.MAG, 5)
					.setInitiative(iniFactory.makeDefaultInitiative(builder))
					.build();
			System.out.println("Created:\n" + ono);
			builder = Combatant.newBuilder(combat);
			Combatant stitch = builder.setName("Stitch")
					.setAttributes(AttributeFactory.makeStandardSpecimen(Race.HUMAN))
					.setInitiative(iniFactory.makeDefaultInitiative(builder)).build();
			System.out.println("Created:\n" + stitch);
		} catch (Exception e) {
			System.err.println("Caught Exception " + e);
		}
	}

}
