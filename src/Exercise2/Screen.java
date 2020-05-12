package Exercise2;

import java.util.concurrent.Semaphore;

public class Screen {
	private static final Semaphore screenSemaphore = new Semaphore(1, true);
	
	public static void print(String str) {
		try {
			screenSemaphore.acquire();
		} catch (InterruptedException e) {}
		System.out.println(str);
		screenSemaphore.release();
	}
}
