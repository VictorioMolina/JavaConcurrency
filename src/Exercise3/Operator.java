package Exercise3;

public class Operator extends Thread {
	private int id;
	private int operation;
	private TernarySequencer ternarySequencer;

	public Operator(int id, int operation, TernarySequencer ternarySequencer) {
		this.id = id;
		this.operation = operation;
		this.ternarySequencer = ternarySequencer;
	}

	public void run() {
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep((int) Math.random() * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			switch (operation) {
			case 1:
				try {
					ternarySequencer.operation1(id);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try {
					ternarySequencer.operation2(id);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case 3:
				try {
					ternarySequencer.operation3(id);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		}
	}
}
