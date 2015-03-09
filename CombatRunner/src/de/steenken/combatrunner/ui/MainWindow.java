package de.steenken.combatrunner.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import de.steenken.combatrunner.conf.GlobalConstants;
import de.steenken.combatrunner.model.CombatModel;
import de.steenken.combatrunner.model.Combatant;
import de.steenken.combatrunner.model.CombatModel.Edition;
import de.steenken.combatrunner.model.IncompleteModelException;
import de.steenken.combatrunner.model.attributes.AttributeFactory;
import de.steenken.combatrunner.model.attributes.RaceDefaults.Race;
import de.steenken.combatrunner.model.initiative.InitiativeFactory;

public class MainWindow extends JFrame {

	private static final CombatModel emptyDefaultCombat = new CombatModel(
			Edition.FOURTH);

	private JMenuBar menubar;
	private JMenu combatMenu;
	private JMenuItem newCombat;
	private JMenuItem loadCombat;
	private JMenuItem newCombatant;
	private JMenuItem loadCombatant;
	private JMenuItem run;
	private JMenuItem quit;

	private JTabbedPane tabsPane;

	private JPanel editPanel;
	private JList<Combatant> combatList;

	private JPanel runPanel;

	public MainWindow() {
		construct();
	}

	private void construct() {
		// set basic window properties
		setTitle("CombatRunner v" + GlobalConstants.VERSION);
		setPreferredSize(new Dimension(1024, 768));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// make and add the menu
		menubar = new JMenuBar();
		combatMenu = new JMenu("Combat");
		newCombat = new JMenuItem("New Combat");
		loadCombat = new JMenuItem("Load Combat");
		newCombatant = new JMenuItem("New Combatant");
		loadCombatant = new JMenuItem("Load Combatant");
		run = new JMenuItem("Run!");
		quit = new JMenuItem("Quit");

		menubar.add(combatMenu);
		combatMenu.add(newCombat);
		combatMenu.add(loadCombat);
		combatMenu.add(new JSeparator());
		combatMenu.add(newCombatant);
		combatMenu.add(loadCombatant);
		combatMenu.add(new JSeparator());
		combatMenu.add(run);
		combatMenu.add(new JSeparator());
		combatMenu.add(quit);
		setJMenuBar(menubar);

		// make and add the tabs
		tabsPane = new JTabbedPane(JTabbedPane.TOP);
		editPanel = new JPanel(false);
		runPanel = new JPanel(false);
		tabsPane.addTab("Edit Combat", editPanel);
		tabsPane.addTab("Run Combat", runPanel);
		add(tabsPane);

		// construct the edit panel
		constructEditPanel();

		// construct run panel
		constructRunPanel();

		pack();
		setVisible(true);
	}

	private void constructRunPanel() {
		runPanel.add(new JLabel("Under Construction"));
	}

	private Combatant makeTestCombatant() {
		Combatant.CombatantBuilder builder = Combatant
				.newBuilder(emptyDefaultCombat);
		try {
			return builder
					.setAttributes(
							AttributeFactory.makeStandardSpecimen(Race.TROLL))
					.setInitiative(
							InitiativeFactory.getFourthEditionFactory()
									.makeDefaultInitiative(builder))
					.setName("Testy Nicky").build();
		} catch (IncompleteModelException e) {
			throw new RuntimeException("this will never happen");
		}
	}

	private void constructEditPanel() {

		emptyDefaultCombat.addCombatant(makeTestCombatant());
		combatList = new JList<>(emptyDefaultCombat);
		combatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		combatList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		combatList.setVisibleRowCount(-1);
		combatList.setPreferredSize(new Dimension(200, 0));
		JScrollPane listScroller = new JScrollPane(combatList);
		editPanel.setLayout(new BorderLayout());
		editPanel.add(listScroller, BorderLayout.WEST);
	}

}
