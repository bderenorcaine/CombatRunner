package de.steenken.combatrunner.test;

import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.CombatModel.Edition;
import de.steenken.combatrunner.model.Combatant.CombatantBuilder;
import de.steenken.combatrunner.model.attributes.AttributeFactory;
import de.steenken.combatrunner.model.attributes.Attribute.Name;
import de.steenken.combatrunner.model.attributes.RaceDefaults.Race;
import de.steenken.combatrunner.model.initiative.InitiativeFactory;
import de.steenken.combatrunner.persistence.CombatRunnerPersistenceLayer;

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
			System.out.println("Created:\n" + ono.fullDescription());
			builder = Combatant.newBuilder(combat);
			Combatant stitch = builder.setName("Stitch")
					.setAttributes(AttributeFactory.makeStandardSpecimen(Race.HUMAN))
					.setInitiative(iniFactory.makeDefaultInitiative(builder)).build();
			System.out.println("Created:\n" + stitch.fullDescription());
			System.out.println("Saving Ono and Stitch to individual files...");
			CombatRunnerPersistenceLayer.saveCombatant(ono, "ono.com");
			CombatRunnerPersistenceLayer.saveCombatant(stitch, "stitch.com");
			System.out.println("Done. Loading Ono and Stitch from those files...");
			ono = CombatRunnerPersistenceLayer.loadCombatant("ono.com", combat);
			stitch = CombatRunnerPersistenceLayer.loadCombatant("stitch.com", combat);
			System.out.println("Done. Let's see if they survived this:");
			System.out.println("Loaded:\n" + ono.fullDescription());
			System.out.println("Loaded:\n" + stitch.fullDescription());
		} catch (Exception e) {
			System.err.println("Caught Exception " + e);
		}
	}

}
