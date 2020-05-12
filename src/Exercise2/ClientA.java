package Exercise2;

public class ClientA extends Client {
	public ClientA(int id, int buyTime, int boxTime) {
		super(id, buyTime, boxTime);
	}

	@Override
	public void selectBox() {
		selectedBox = (int) (Math.random() * SupermarketA.NUMBER_OF_BOXES);
	}

	@Override
	public void waitInQueue() {
		try {
			// The client want to enter the box...
			queueTimeStart = System.currentTimeMillis();
			SupermarketA.boxSemaphores[selectedBox].acquire();
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void payInBox() {
		super.payInBox();

		// The client leaves the box...
		SupermarketA.boxSemaphores[selectedBox].release();
	}

	@Override
	public void run() {
		// The client is buying...
		buy();

		// The client select a random box...
		selectBox();

		// The clients waits in his selected box queue
		waitInQueue();

		// The client is paying the bill in the respective box...
		payInBox();

		// Print the results on screen
		Screen.print(String.format("%s\n%s\n%s\n%s\n\n", "Customer " + id + " served in box " + selectedBox + " -----",
				"Buy time: " + buyTime + " milliseconds", "Time in box: " + boxTime + " milliseconds",
				"Waiting time in the box queue: " + getQueueTime() + " milliseconds"));
	}
}
