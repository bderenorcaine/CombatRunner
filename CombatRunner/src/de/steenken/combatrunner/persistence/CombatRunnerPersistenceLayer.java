package de.steenken.combatrunner.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.CombatModel.Edition;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.Combatant.CombatantBuilder;
import de.steenken.combatrunner.model.EditionMisMatchError;
import de.steenken.combatrunner.model.IncompleteModelException;
import de.steenken.combatrunner.model.attributes.Attribute.Name;
import de.steenken.combatrunner.model.attributes.AttributeArray;
import de.steenken.combatrunner.model.initiative.Initiative;
import de.steenken.combatrunner.persistence.CombatRunnerFile.Attribute;
import de.steenken.combatrunner.persistence.CombatRunnerFile.attribute_array;
import de.steenken.combatrunner.persistence.CombatRunnerFile.combat;
import de.steenken.combatrunner.persistence.CombatRunnerFile.combatant;
import de.steenken.combatrunner.persistence.CombatRunnerFile.file;
import de.steenken.combatrunner.persistence.CombatRunnerFile.initiative;
import de.steenken.combatrunner.ui.MainWindow;

public final class CombatRunnerPersistenceLayer {

	private CombatRunnerPersistenceLayer() {

	}

	public static Combatant loadCombatant(String filename, CombatModel model)
			throws FileNotFoundException {
		// try to open the file
		try (FileInputStream inputStream = new FileInputStream(filename)) {
			CombatRunnerFile.file file = CombatRunnerFile.file
					.parseFrom(inputStream);
			if (file.hasCombatant()) {
				return readCombatant(file.getCombatant(), model);
			} else {
				throw new NoSuchElementException();
			}
		} catch (IOException e) {
			MainWindow.getMainWindow().errorMessage(
					"IOException while trying to read file \"" + filename
							+ "\"\\Message reads: " + e.getLocalizedMessage());
			return null;
		} catch (IncompleteModelException e) {
			MainWindow.getMainWindow().errorMessage(
					"IncompleteModelException while trying to read file \""
							+ filename
							+ "\"\\This means that the file is broken.");
			return null;
		} catch (NoSuchElementException e) {
			MainWindow.getMainWindow().errorMessage(
					"The file \"" + filename
							+ "\" does not contain a stand-alone combatant.");
			return null;
		}
	}
	
	public static void saveCombatant(Combatant combatant, String filename) {
		try (FileOutputStream outputStream = new FileOutputStream(filename)) {
			file.Builder builder = file.newBuilder();
			builder.setCombatant(writeCombatant(combatant));
			builder.build().writeTo(outputStream);
		} catch (IOException e) {
			MainWindow.getMainWindow().errorMessage(
					"IOException while trying to write file \"" + filename
							+ "\"\\Message reads: " + e.getLocalizedMessage());
		}
	}

	public static CombatModel readCombat(CombatRunnerFile.combat input)
			throws IncompleteModelException {
		CombatModel combat = new CombatModel(Edition.valueOf(input.getEdition()
				.name()));
		combat.setName(input.getName());
		for (CombatRunnerFile.combatant combatant : input.getParticipantsList()) {
			combat.addCombatant(readCombatant(combatant, combat));
		}
		return combat;
	}

	public static Combatant readCombatant(CombatRunnerFile.combatant input,
			CombatModel combat) throws IncompleteModelException {
		CombatantBuilder builder = Combatant.newBuilder(combat);
		builder.setAttributes(readAttributes(input.getAttributes()));
		builder.setName(input.getName());
		builder.setInitiative(readInitiative(input, builder, combat));
		return builder.build();
	}

	private static Initiative readInitiative(combatant input,
			CombatantBuilder builder, CombatModel combat) {
		if (Edition.valueOf(input.getEdition().name()) != combat.getEdition()) {
			throw new EditionMisMatchError();
		}
		return combat.getInitiativeFactory().makeInitiative(builder,
				input.getInitiative().getValue());
	}

	private static AttributeArray readAttributes(attribute_array attributes) {
		AttributeArray arrayRead = new AttributeArray();
		for (int i = 0; i < attributes.getAttributesCount(); ++i) {
			arrayRead.setValue(
					Name.valueOf(attributes.getAttributes(i).name()),
					attributes.getValues(i));
		}
		return arrayRead;
	}

	public static combat writeCombat(final CombatModel model) {
		combat.Builder builder = combat.newBuilder();
		builder.setEdition(CombatRunnerFile.Edition.valueOf(model.getEdition()
				.name()));
		builder.setName(model.getName());
		for (Combatant combatant : model) {
			builder.addParticipants(writeCombatant(combatant));
		}
		return builder.build();
	}

	public static combatant writeCombatant(final Combatant c) {
		combatant.Builder builder = combatant.newBuilder();
		builder.setEdition(CombatRunnerFile.Edition.valueOf(c.getCombat()
				.getEdition().name()));
		builder.setInitiative(writeInitiative(c.getInitiative()));
		builder.setAttributes(writeAttributes(c.getAttributes()));
		builder.setName(c.getName());
		return builder.build();
	}

	private static initiative writeInitiative(final Initiative ini) {
		initiative.Builder builder = initiative.newBuilder();
		builder.setValue(ini.getEditionAgnosticValue());
		return builder.build();
	}

	private static attribute_array writeAttributes(AttributeArray array) {
		attribute_array.Builder builder = attribute_array.newBuilder();
		for (Attribute attr : Attribute.values()) {
			de.steenken.combatrunner.model.attributes.Attribute.Name name = de.steenken.combatrunner.model.attributes.Attribute.Name
					.valueOf(attr.name());
			if (array.hasValue(name)) {
				builder.addAttributes(attr).addValues(array.getValue(name));
			}
		}
		return builder.build();
	}
}
