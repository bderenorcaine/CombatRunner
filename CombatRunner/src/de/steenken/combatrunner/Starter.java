package de.steenken.combatrunner;

import de.steenken.combatrunner.ui.MainWindow;

public class Starter {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }

            private MainWindow window;
            
			private void createAndShowGUI() {
				window = new MainWindow();
			}
        });
	}

}
