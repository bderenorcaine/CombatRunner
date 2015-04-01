package de.steenken.combatrunner.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.IncompleteModelException;
import de.steenken.combatrunner.model.attributes.Attribute;
import de.steenken.combatrunner.model.attributes.AttributeArray;
import de.steenken.combatrunner.model.attributes.MetatypeDefaults;
import de.steenken.combatrunner.model.attributes.MetatypeDefaults.Metatype;
import de.steenken.combatrunner.model.initiative.FifthEditionInitiativeFactory;
import de.steenken.combatrunner.model.initiative.FourthEditionInitiativeFactory;
import de.steenken.combatrunner.model.initiative.Initiative;
import de.steenken.combatrunner.persistence.CombatRunnerPersistenceLayer;

public class CombatantEditor extends JFrame implements ItemListener,
		ActionListener {

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
		private JButton resetButton = new JButton("Reset to metatype Defaults");
		private JPanel inputPanel = new JPanel();

		private AttributeEditor() {
			inputPanel.setLayout(new GridLayout(2,
					Attribute.Name.values().length));
			for (Attribute.Name attr : Attribute.Name.values()) {
				labels.put(attr, new JLabel(attr.name()));
				if ((attr != Attribute.Name.PDa)
						&& (attr != Attribute.Name.SDa)
						&& (attr != Attribute.Name.MAG)
						&& (attr != Attribute.Name.RES)) {
					inputs.put(attr, new JSpinner(new SpinnerNumberModel(1, 1,
							12, 1)));
				} else if ((attr == Attribute.Name.PDa)
						|| (attr == Attribute.Name.SDa)) {
					inputs.put(attr, new JSpinner(new SpinnerNumberModel(0, 0,
							15, 1)));
				} else {
					inputs.put(attr, new JSpinner(new SpinnerNumberModel(0, 0,
							12, 1)));
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
			// resetButton.setPreferredSize(new Dimension(Short.MAX_VALUE,
			// resetButton.getHeight()));
			add(resetButton);
			resetButton.addActionListener(CombatantEditor.this);

			pack();
		}

		public void setToMetatypeDefault(Metatype type) {
			for (Attribute.Name attr : Attribute.Name.values()) {
				try {
					inputs.get(attr).setValue(
							MetatypeDefaults.getValue(type, attr));
				} catch (NoSuchElementException e) {
					inputs.get(attr).setValue(0);
				}
			}
		}

		public AttributeArray buildAttributeArray() {
			AttributeArray array = new AttributeArray();
			for (Attribute.Name attr : Attribute.Name.values()) {
				array.setValue(attr, (Integer) inputs.get(attr).getValue());
			}
			return array;
		}
	}

	private FourthEdInitiativeEditor editor4th = new FourthEdInitiativeEditor();
	private FifthEdInitiativeEditor editor5th = new FifthEdInitiativeEditor();

	private abstract class InitiativeEditor extends JPanel {
		protected JSpinner iniPicker;
		protected JSpinner bonusPicker;

		public abstract Initiative buildInitiative(final Combatant.CombatantBuilder builder);
	}

	private class FourthEdInitiativeEditor extends InitiativeEditor {
		public FourthEdInitiativeEditor() {
			iniPicker = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
			iniPicker
					.setToolTipText("The number of initiative passes the combatant has");
			bonusPicker = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
			bonusPicker
					.setToolTipText("The bonus to basic initiative (INT+REA) the combatant receives");
			setLayout(new GridLayout(2, 2));
			add(new JLabel("Passes"));
			add(new JLabel("Bonus"));
			add(iniPicker);
			add(bonusPicker);

			pack();
		}

		@Override
		public Initiative buildInitiative(
				final Combatant.CombatantBuilder builder) {
			return FourthEditionInitiativeFactory.getFourthEditionFactory()
					.makeInitiative(builder, (Integer) iniPicker.getValue(),
							(Integer) bonusPicker.getValue());
		}

	}

	private class FifthEdInitiativeEditor extends InitiativeEditor {
		public FifthEdInitiativeEditor() {
			iniPicker = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
			iniPicker
					.setToolTipText("The number of initiative dice the combatant has");
			bonusPicker = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
			bonusPicker
					.setToolTipText("The bonus to basic initiative (INT+REA) the combatant receives");
			setLayout(new GridLayout(2, 2));
			add(new JLabel("Dice"));
			add(new JLabel("Bonus"));
			add(iniPicker);
			add(bonusPicker);

			pack();
		}

		@Override
		public Initiative buildInitiative(
				final Combatant.CombatantBuilder builder) {
			return FifthEditionInitiativeFactory.getFifthEditionFactory()
					.makeInitiative(builder, (Integer) iniPicker.getValue(),
							(Integer) bonusPicker.getValue());
		}
	}

	private JPanel nameAndmetatype;
	private JTextField name;
	private JComboBox<MetatypeDefaults.Metatype> metatypePicker;

	private AttributeEditor attributeEditor;

	private JPanel initiativePanel;
	private JComboBox<Edition> editionPicker;
	private JPanel initiativeEditor;

	private JPanel buttonPanel;
	private JButton deleteButton;
	private JButton discardButton;
	private JButton saveButton;

	@Override
	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout) (initiativeEditor.getLayout());
		cl.show(initiativeEditor, ((Edition) evt.getItem()).name());
	}

	private void construct() {
		// set basic window properties
		setTitle("Edit Combatant");
		setPreferredSize(new Dimension(500, 400));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		// construct the name and metatype panel
		nameAndmetatype = new JPanel();
		nameAndmetatype.setLayout(new GridLayout(2, 2));
		nameAndmetatype.add(new JLabel("Name:"));
		nameAndmetatype.add(name = new JTextField("New Combatant"));
		nameAndmetatype.add(new JLabel("Metatype:"));
		nameAndmetatype.add(metatypePicker = new JComboBox<>(
				MetatypeDefaults.Metatype.values()));
		getContentPane().add(nameAndmetatype);
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
		editionPicker.addItemListener(this);
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
		deleteButton.addActionListener(this);
		discardButton.addActionListener(this);
		saveButton.addActionListener(this);
		getContentPane().add(buttonPanel);

		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() instanceof JButton) {
			if (arg0.getSource() == attributeEditor.resetButton) {
				if (JOptionPane.showConfirmDialog(this,
						"Reset to the defaults for the chosen metatype?",
						"Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
					attributeEditor
							.setToMetatypeDefault((Metatype) metatypePicker
									.getSelectedItem());
				}
			} else if (arg0.getSource() == saveButton) {
				JFileChooser chooser = new JFileChooser();
				int choice = chooser.showSaveDialog(this);
				if (choice == JFileChooser.APPROVE_OPTION) {
					try {
						Combatant combatant = buildCombatant();
						CombatRunnerPersistenceLayer.saveCombatant(combatant,
								chooser.getSelectedFile().getAbsolutePath());
					} catch (IOException e) {
						JOptionPane.showMessageDialog(this,
								"Combatant could not be saved due to an I/O error\n"
										+ e.getMessage(), "error",
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this,
								"Combatant could not be saved due to an unexpected error\n"
										+ e.getMessage(), "error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(this, "Combatant not saved.");
				}
			}
		}
	}

	private Combatant buildCombatant() throws IncompleteModelException {
		Combatant.CombatantBuilder builder = Combatant.newBuilder(MainWindow.getMainWindow().getCombat());
		builder.setAttributes(attributeEditor.buildAttributeArray());
		builder.setName(name.getText());
		if (((Edition) editionPicker.getSelectedItem()).equals(Edition.FOURTH)) {
			builder.setInitiative(editor4th.buildInitiative(builder));
		} else {
			builder.setInitiative(editor5th.buildInitiative(builder));
		}
		return builder.build();
	}

	private void connect() {

	}

	public static void main(String[] args) {
		CombatantEditor testWindow = getCombatantEditor();
	}

}
