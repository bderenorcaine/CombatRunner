package de.steenken.combatrunner.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import de.steenken.combatrunner.conf.GlobalConstants;
import de.steenken.combatrunner.model.CombatModel.Edition;
import de.steenken.combatrunner.model.attributes.Attribute;
import de.steenken.combatrunner.model.attributes.RaceDefaults;

public class CombatantEditor extends JFrame {

	private static CombatantEditor singleton = null;

	public static CombatantEditor getCombatantEditor() {
		if (singleton == null) {
			singleton = new CombatantEditor();
		}
		return singleton;
	}

	private CombatantEditor() {
		construct();
		connect();
	}
	
	private void drawBorder(JComponent component) {
		component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.red),
                component.getBorder()));
	}

	private class AttributeEditor extends JPanel {
		private HashMap<Attribute.Name, JLabel> labels = new HashMap<>();
		private HashMap<Attribute.Name, JSpinner> inputs = new HashMap<>();
		private JButton resetButton = new JButton("Reset to Race Defaults");
		private JPanel inputPanel = new JPanel();
		
		private AttributeEditor() {
			inputPanel.setLayout(new GridLayout(2, Attribute.Name.values().length));
			for (Attribute.Name attr : Attribute.Name.values()) {
				labels.put(attr, new JLabel(attr.name()));
				if ((attr != Attribute.Name.PDa) && (attr != Attribute.Name.SDa)) {
					inputs.put(attr, new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)));
				} else {
					inputs.put(attr, new JSpinner(new SpinnerNumberModel(0, 0, 15, 1)));
				}
			}
			for (Attribute.Name attr : Attribute.Name.values()) {
				inputPanel.add(labels.get(attr));
			}
			for (Attribute.Name attr : Attribute.Name.values()) {
				inputPanel.add(inputs.get(attr));
			}
			setLayout(new GridLayout(2, 1));
			add(inputPanel);
//			resetButton.setPreferredSize(new Dimension(Short.MAX_VALUE, resetButton.getHeight()));
			add(resetButton);
		}
	}

	private FourthEdInitiativeEditor editor4th = new FourthEdInitiativeEditor();
	private FifthEdInitiativeEditor editor5th = new FifthEdInitiativeEditor();

	private abstract class InitiativeEditor extends JPanel {
		protected JSpinner iniPicker;
		protected JSpinner bonusPicker;
		
	}

	private class FourthEdInitiativeEditor extends InitiativeEditor {

	}

	private class FifthEdInitiativeEditor extends InitiativeEditor {

	}

	private JPanel nameAndRace;
	private JTextField name;
	private JComboBox<RaceDefaults.Race> racePicker;

	private AttributeEditor attributeEditor;

	private JPanel initiativePanel;
	private JComboBox<Edition> editionPicker;
	private JPanel initiativeEditor;

	private JPanel buttonPanel;
	private JButton deleteButton;
	private JButton discardButton;
	private JButton saveButton;
	
	private void construct() {
		// set basic window properties
		setTitle("Edit Combatant");
		setPreferredSize(new Dimension(500, 400));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		// construct the name and race panel
		nameAndRace = new JPanel();
		nameAndRace.setLayout(new GridLayout(2, 2));
		nameAndRace.add(new JLabel("Name:"));
		nameAndRace.add(name = new JTextField("New Combatant"));
		nameAndRace.add(new JLabel("Race:"));
		nameAndRace
				.add(racePicker = new JComboBox<>(RaceDefaults.Race.values()));
		getContentPane().add(nameAndRace);
		getContentPane().add(new JSeparator(SwingConstants.HORIZONTAL));

		// construct and add the attributes editor
		attributeEditor = new AttributeEditor();
		getContentPane().add(attributeEditor);
		getContentPane().add(new JSeparator(SwingConstants.HORIZONTAL));

		// construct and add the initiative panel
		initiativePanel = new JPanel();
		initiativePanel.setLayout(new GridLayout(1, 2));
		initiativePanel.add(editionPicker = new JComboBox<Edition>(Edition
				.values()));
		initiativeEditor = new JPanel();
		initiativeEditor.setLayout(new CardLayout());
		initiativeEditor.add(editor4th, Edition.FOURTH.name());
		initiativeEditor.add(editor5th, Edition.FIFTH.name());
		initiativePanel.add(initiativeEditor);
		drawBorder(initiativePanel);
		drawBorder(initiativeEditor);
		drawBorder(editionPicker);
		getContentPane().add(initiativePanel);
		getContentPane().add(new JSeparator(SwingConstants.HORIZONTAL));
		
		// construct and add the button panel
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 3));
		buttonPanel.add(deleteButton = new JButton("Discard Character"));
		buttonPanel.add(discardButton = new JButton("Discard Changes"));
		buttonPanel.add(saveButton = new JButton("Save Changes"));
		getContentPane().add(buttonPanel);
		
		pack();
		setVisible(true);
	}

	private void connect() {

	}
	
	public static void main(String[] args) {
		CombatantEditor testWindow = getCombatantEditor();
	}

}
