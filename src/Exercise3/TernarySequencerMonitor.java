package Exercise3;

public class TernarySequencerMonitor {
	/**
	 * Main method that runs when the program is started.
	 */
	public static void main(String[] args) {
		TernarySequencer ternarySequencer = new TernarySequencer();

		// Create 4 operators of each operation
		for (int i = 0; i < 12; i++) {
			Operator o = new Operator(i, (i % 3 + 1), ternarySequencer);
			o.start();
		}
	}
}
