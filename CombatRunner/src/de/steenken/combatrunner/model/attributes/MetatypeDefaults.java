package de.steenken.combatrunner.model.attributes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.steenken.combatrunner.model.attributes.Attribute.Name;

public class MetatypeDefaults {
	
	public static final String RESFILENAME = "res/metatypedefaults.res";
	
	public enum Metatype {
		HUMAN,
		ELF,
		DWARF,
		ORC,
		TROLL;
	}
	
	private MetatypeDefaults() {	
	}
	
	private static HashMap<Metatype, AttributeArray> defaults = initializeInstance();
	
	private static HashMap<Metatype, AttributeArray> initializeInstance() {
		HashMap<Metatype, AttributeArray> map = new HashMap<>();
		try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(RESFILENAME)))) {
			while (input.ready()) {
				String type = input.readLine();
				AttributeArray array = new AttributeArray();
				for (int i = 0; i < 9; ++i) {
					String line = input.readLine();
					String[] pair = line.split(",");
					array.setValue(Name.valueOf(pair[0]), Integer.parseInt(pair[1]));
				}
				map.put(Metatype.valueOf(type), array);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error - cannot open metatype defaults file \"" + System.getProperty("user.dir") + RESFILENAME + "\"");
		} catch (IOException e) {
			throw new RuntimeException("Error - IOException (" + e + ") encountered while parsing metatype defaults file \"" + System.getProperty("user.dir") + RESFILENAME + "\"");
		} catch (NumberFormatException e) {
			throw new RuntimeException("Error - NumberFormatException (" + e.getMessage() + ") encountered while parsing metatype defaults file \"" + System.getProperty("user.dir") + RESFILENAME + "\"");
		}
		return map;
	}
	
	public static final int getValue(Metatype type, Name name) {
		return defaults.get(type).getValue(name);
	}
	
	public static final AttributeArray getDefaults(Metatype race) {
		return defaults.get(race).copy();
	}
}
