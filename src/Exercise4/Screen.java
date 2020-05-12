package Exercise4;

import java.util.Random;

import messagepassing.MailBox;
import messagepassing.Selector;

public class Screen extends Thread {
	public static MailBox mutex = new MailBox();
	private boolean busy;
	private Selector selector;
	private String msg;

	public Screen() {
		busy = false;
		selector = new Selector();
		selector.addSelectable(mutex, false);
		msg = "";
	}

	@Override
	public void run() {
		Random r = new Random();
		while (true) {
			mutex.setGuardValue(true);
			switch (selector.selectOrBlock()) {
			case 1:
				if (!busy) {
					msg = (String) mutex.receive();
					busy = true;
					System.out.println(msg);
					try {
						Thread.sleep(r.nextInt(2) * 500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					busy = false;
				}
				break;
			default:
				break;
			}
		}

	}
}
