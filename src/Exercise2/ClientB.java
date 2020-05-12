package Exercise2;

public class ClientB extends Client {
	public ClientB(int id, int buyTime, int boxTime) {
		super(id, buyTime, boxTime);
	}

	@Override
	public void selectBox() {
		selectedBox = SupermarketB.FreeBoxesManager.popFirsFreeBox();
	}

	@Override
	public void waitInQueue() {
		try {
			// The client want to enter the box...
			queueTimeStart = System.currentTimeMillis();
			SupermarketB.freeBoxSemaphore.acquire();
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void payInBox() {
		try {
			// The client leaves the queue and enter to the selected box...
			queueTimeEnd = System.currentTimeMillis();
			Thread.sleep(boxTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// The client leaves the box...
		SupermarketB.FreeBoxesManager.pushFreeBox(selectedBox);
		SupermarketB.freeBoxSemaphore.release();
	}

	@Override
	public void run() {
		// The client is buying...
		buy();

		// The clients waits in the queue
		waitInQueue();

		// The client selects the free box
		selectBox();

		// The client is paying the bill in the respective box...
		payInBox();

		// Print the results on screen
		Screen.print(String.format("%s\n%s\n%s\n%s\n\n", "Customer " + id + " served in box " + selectedBox + " -----",
				"Buy time: " + buyTime + " milliseconds", "Time in box: " + boxTime + " milliseconds",
				"Waiting time in the box queue: " + getQueueTime() + " milliseconds"));
	}
}
