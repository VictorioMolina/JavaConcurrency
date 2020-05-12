package Exercise2;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class SupermarketB extends Supermarket {
	public static Semaphore freeBoxSemaphore = new Semaphore(NUMBER_OF_BOXES);
	private static LinkedList<Integer> freeBoxes = new LinkedList<>();

	public SupermarketB(Random r) {
		this.r = r;
	}

	// Class to manage the free boxes
	static class FreeBoxesManager {
		private final static Semaphore mutex = new Semaphore(1, true);

		public static void pushFreeBox(int box) {
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
			}

			freeBoxes.add(box);
			mutex.release();
		}

		public static int popFirsFreeBox() {
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
			}

			// Pop the first free box of the list
			int freeBox = freeBoxes.removeFirst();
			mutex.release();

			return freeBox;
		}
	}

	public void work() {
		// Initializing the "free boxes" queue
		for (int i = 0; i < NUMBER_OF_BOXES; i++) {
			freeBoxes.add(i);
		}

		// Creating and starting 50 Threads of Clients
		clients = new Client[50];
		for (int i = 0; i < clients.length; i++) {
			int buyTime = r.nextInt(10000) + 1; // From 1 to 1000 millisecs.
			int boxTime = r.nextInt(10000) + 1; // From 1 to 1000 millisecs.

			clients[i] = new ClientB(i, buyTime, boxTime);
			clients[i].start();
		}

		// Wait all threads to finish
		for (Client client : clients) {
			try {
				client.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Calculate the client average waiting time in box
		int totalWaitingTime = 0;
		for (Client client : clients) {
			totalWaitingTime += client.getQueueTime() + client.getBoxTime();
		}

		System.out.println(String.format("%s\n%s\n\n", "Main thread -----",
				"Average waiting time at customers' checkout: " + totalWaitingTime / clients.length + " milliseconds"));
	}
}