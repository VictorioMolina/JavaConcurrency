package Exercise2;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class SupermarketA extends Supermarket {
	public static Semaphore[] boxSemaphores = new Semaphore[NUMBER_OF_BOXES];

	public SupermarketA(Random r) {
		this.r = r;
	}

	@Override
	public void work() {
		// Creating NUMBER_OF_BOXES semaphores
		for (int i = 0; i < NUMBER_OF_BOXES; i++) {
			boxSemaphores[i] = new Semaphore(1, true);
		}

		// Creating and starting 50 Threads of Clients
		clients = new Client[50];
		for (int i = 0; i < clients.length; i++) {
			int buyTime = r.nextInt(10000) + 1; // From 1 to 1000 millisecs.
			int boxTime = r.nextInt(10000) + 1; // From 1 to 1000 millisecs.
			
			clients[i] = new ClientA(i, buyTime, boxTime);
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