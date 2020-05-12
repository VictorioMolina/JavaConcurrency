package Exercise2;

import java.util.Random;

public class OpenSupermarkets {
	/**
	 * Main method that runs when the program is started.
	 */
	public static void main(String[] args) {
		Random r;
		
		// Supermarket A
		System.out.println("---------- Supermarket A ----------");
		r = new Random(1); // Initializating r with seed = 1
		SupermarketA a = new SupermarketA(r);
		a.work();
		
		System.out.println();
		
		// Supermarket B
		System.out.println("---------- Supermarket B ----------");
		r = new Random(1); // Re-initializating r with seed = 1
		SupermarketB b = new SupermarketB(r);
		b.work();
	}
}
