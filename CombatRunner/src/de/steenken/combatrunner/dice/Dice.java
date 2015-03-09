package de.steenken.combatrunner.dice;

import java.util.Date;
import java.util.Random;

public class Dice {

	public static class DiceException extends Exception {

	}

	public static class SingleDieException extends DiceException {

	}

	public static class MultipleDiceException extends DiceException {

	}

	public static class DieExploded extends SingleDieException {
	}

	public static class DieFailed extends SingleDieException {

	}

	public static class Glitch extends MultipleDiceException {
		private final int successesRolled;

		public Glitch(final int successesRolled) {
			this.successesRolled = successesRolled;
		}

		public final int getSuccessesRolled() {
			return successesRolled;
		}
	}

	public static class CriticalGlitch extends MultipleDiceException {

	}

	private static final int SUCCESS_LEVEL = 5;

	public static final int DICE_SIDES = 6;
	
	private static final int[] statistics = new int[DICE_SIDES];

	private static Random pseudorandom = new Random(new Date().getTime());

	private static int rollSingleDie() throws SingleDieException {
		int result = pseudorandom.nextInt(6) + 1;
		statistics[result - 1] += 1;
		if (result == DICE_SIDES) {
			throw new DieExploded();
		} else if (result == 1) {
			throw new DieFailed();
		} else {
			return result;
		}
	}
	
	public static int rollDiceForSum(final int pool) {
		int result = 0;
		for (int i = 0; i < pool; ++i) {
			try {
				result += rollSingleDie();
			} catch (DieExploded e) {
				result += 6;
			} catch (DieFailed e) {
				result += 1;
			} catch (SingleDieException e) {
				throw new RuntimeException("Error - unknown single die Exception " + e);
			}
		}
		return result;
	}

	private static boolean rollSingleDieForSuccess() throws DieFailed {
		try {
			if (rollSingleDie() >= SUCCESS_LEVEL) {
				return true;
			} else {
				return false;
			}
		} catch (DieExploded e) {
			return true;
		} catch (SingleDieException e) {
			throw (DieFailed) e;
		}
	}

	private static boolean rollSingleExplodingDieForSuccess()
			throws SingleDieException {
		if (rollSingleDie() >= SUCCESS_LEVEL) {
			return true;
		} else {
			return false;
		}
	}

	public static final int getSuccesses(final int pool, final int edge)
			throws MultipleDiceException {
		int successesRolled = 0;
		int onesRolled = 0;
		int dice = pool + edge;
		while (dice-- > 0) {
			try {
				if (edge > 0) {
					if (rollSingleExplodingDieForSuccess()) {
						successesRolled++;
					}
				} else {
					if (rollSingleDieForSuccess()) {
						successesRolled++;
					}
				}
			} catch (DieFailed e) {
				onesRolled++;
			} catch (DieExploded e) {
				System.err.println("Die Exploded");
				successesRolled++;
				dice++;
			} catch (SingleDieException e) {
				throw new RuntimeException("Error - Unhandled Exception " + e);
			}
		}
		if (onesRolled >= Math.ceil(pool / 2.0)) {
			if (successesRolled == 0) {
				throw new CriticalGlitch();
			} else {
				throw new Glitch(successesRolled);
			}
		}
		return successesRolled;
	}

	public static final int getSuccesses(final int pool)
			throws MultipleDiceException {
		return getSuccesses(pool, 0);
	}
}
