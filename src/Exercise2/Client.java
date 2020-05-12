package Exercise2;

public abstract class Client extends Thread {
	protected long id;
	protected int buyTime; // Time in milliseconds
	protected int boxTime; // Time in milliseconds
	protected int selectedBox;
	protected long queueTimeStart;
	protected long queueTimeEnd;

	public Client(int id, int buyTime, int boxTime) {
		this.id = id;
		this.buyTime = buyTime;
		this.boxTime = boxTime;
		queueTimeStart = 0;
		queueTimeEnd = 0;
	}

	public abstract void selectBox();

	public void buy() {
		try {
			Thread.sleep(buyTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public abstract void waitInQueue();

	public void payInBox() {
		try {
			// The client leaves the queue and enter to the box...
			queueTimeEnd = System.currentTimeMillis();
			Thread.sleep(boxTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public abstract void run();

	public int getBoxTime() {
		return boxTime;
	}

	public int getQueueTime() {
		return (int) (queueTimeEnd - queueTimeStart);
	}
}
