package de.steenken.combatrunner.model.attributes;

import java.util.HashMap;
import java.util.NoSuchElementException;

import de.steenken.combatrunner.model.attributes.Attribute.Name;

public class AttributeArray {
	private HashMap<Attribute.Name, Attribute> array = new HashMap<Attribute.Name, Attribute>();

	public int getValue(Attribute.Name name) {
		if (array.containsKey(name)) {
			return array.get(name).getValue();
		} else {
			throw new NoSuchElementException("Attribute " + name
					+ " not defined.");
		}
	}

	public int getValue(Attribute.Name name, int defaultValue) {
		if (array.containsKey(name)) {
			return array.get(name).getValue();
		} else {
			return defaultValue;
		}
	}

	public void setValue(final Attribute.Name name, final int value) {
		if (!array.containsKey(name)) {
			array.put(name, new Attribute(value));
		} else {
			array.get(name).setValue(value);
		}
	}
	
	public final AttributeArray copy() {
		AttributeArray copy = new AttributeArray();
		copy.array.putAll(array);
		return copy;
	}

	public boolean minComplete() {
		return true;
	}

	public final String toString() {
		String result = new String();
		result += "+-----------+";
		result += '\n';
		result += "|BARSCILWEMR|";
		result += '\n';
		result += "|";
		result += getValue(Name.BOD, 0);
		result += getValue(Name.AGI, 0);
		result += getValue(Name.REA, 0);
		result += getValue(Name.STR, 0);
		result += getValue(Name.CHA, 0);
		result += getValue(Name.INT, 0);
		result += getValue(Name.LOG, 0);
		result += getValue(Name.WIL, 0);
		result += getValue(Name.EDG, 0);
		result += getValue(Name.MAG, 0);
		result += getValue(Name.RES, 0);
		result += "|";
		result += '\n';
		result += "+-----------+";
		result += '\n';
		return result;
	}
}
