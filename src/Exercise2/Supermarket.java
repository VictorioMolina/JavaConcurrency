package Exercise2;

import java.util.Random;

public abstract class Supermarket {
	protected static final int NUMBER_OF_BOXES = 10;
	protected Client[] clients;

	// Random seed
	protected Random r;

	protected abstract void work();
}
