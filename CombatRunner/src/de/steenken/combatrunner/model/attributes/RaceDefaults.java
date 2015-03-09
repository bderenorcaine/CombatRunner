package de.steenken.combatrunner.model.attributes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import de.steenken.combatrunner.model.attributes.Attribute.Name;

public class RaceDefaults {
	
	public static final String RESFILENAME = "res/racedefaults.res";
	
	public enum Race {
		HUMAN,
		ELF,
		DWARF,
		ORC,
		TROLL;
	}
	
	private RaceDefaults() {	
	}
	
	private static HashMap<Race, AttributeArray> defaults = initializeInstance();
	
	private static HashMap<Race, AttributeArray> initializeInstance() {
		HashMap<Race, AttributeArray> map = new HashMap<>();
		try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(RESFILENAME)))) {
			while (input.ready()) {
				String race = input.readLine();
				AttributeArray array = new AttributeArray();
				for (int i = 0; i < 9; ++i) {
					String line = input.readLine();
					String[] pair = line.split(",");
					array.setValue(Name.valueOf(pair[0]), Integer.parseInt(pair[1]));
				}
				map.put(Race.valueOf(race), array);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error - cannot open racial defaults file \"" + System.getProperty("user.dir") + RESFILENAME + "\"");
		} catch (IOException e) {
			throw new RuntimeException("Error - IOException (" + e + ") encountered while parsing racial defaults file \"" + System.getProperty("user.dir") + RESFILENAME + "\"");
		} catch (NumberFormatException e) {
			throw new RuntimeException("Error - NumberFormatException (" + e.getMessage() + ") encountered while parsing racial defaults file \"" + System.getProperty("user.dir") + RESFILENAME + "\"");
		}
		return map;
	}
	
	public static final int getValue(Race race, Name name) {
		return defaults.get(race).getValue(name);
	}
	
	public static final AttributeArray getDefaults(Race race) {
		return defaults.get(race).copy();
	}
}
