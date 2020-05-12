package Exercise4;

import java.io.Serializable;
import java.util.LinkedList;

import messagepassing.MailBox;
import messagepassing.Selector;

class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private int clientId;
	private int level;

	public Message(int clientId, int level) {
		this.clientId = clientId;
		this.level = level;
	}

	public int getClientId() {
		return clientId;
	}

	public int getLevel() {
		return level;
	}
}

class Controller extends Thread {
	private MailBox queries;
	private MailBox[] clients;
	private Selector selector;
	private LinkedList<Message> pendingRequests;

	public Controller(MailBox queries, MailBox[] clients) {
		this.queries = queries;
		this.clients = clients;
		selector = new Selector();
		selector.addSelectable(queries, false);
		pendingRequests = new LinkedList<>();
	}

	public void run() {
		while (true) {
			queries.setGuardValue(true);
			switch (selector.selectOrBlock()) {
			case 1:
				Message msg = (Message) queries.receive();
				pendingRequests.add(msg);
				if (pendingRequests.size() > 1) {
					for (Message m : pendingRequests) {
						if (m != msg && Math.abs(m.getLevel() - msg.getLevel()) <= 2) {
							// Send the found partner to the clients
							clients[msg.getClientId()].send("Plays with player " + m.getClientId() + " ---- with LEVEL "
									+ m.getLevel() + " ----");

							clients[m.getClientId()].send("Plays with player " + msg.getClientId() + " ---- with LEVEL "
									+ msg.getLevel() + " ----");

							// Delete both messages from pendingRequests
							pendingRequests.remove(pendingRequests.indexOf(msg));
							pendingRequests.remove(pendingRequests.indexOf(m));
							break;
						}
					}
				}
				break;
			default:
				break;
			}
		}
	}
}

class Client extends Thread {
	private int id;
	private int level;
	private MailBox partner;
	private MailBox controllerQueries;
	private Selector selector;

	public Client(int id, MailBox partner, MailBox controllerQueries) {
		this.id = id;
		level = 0;
		this.partner = partner;
		this.controllerQueries = controllerQueries;
		selector = new Selector();
		selector.addSelectable(partner, false);
	}

	private void setLevel() {
		level = (int) (Math.random() * 10) + 1;
	}

	private void requestPartner(Message msg) {
		controllerQueries.send(msg);
	}

	public void run() {
		while (true) {
			// Generate level
			setLevel();
			
			// Sending the querie message to the Server Controller
			Message msg = new Message(id, level);

			// Requesting a partner with similar level
			requestPartner(msg);

			partner.setGuardValue(true);
			switch (selector.selectOrBlock()) {
			case 1:
				String str = (String) partner.receive();
				Screen.mutex.send(String.format("%s\n%s\n\n"
						, "Player " + id + " ---- with LEVEL " + level
						, str
				));
				break;
			default:
				break;
			}

		}
	}
}

public class Server {
	private static final int NUMBER_OF_CLIENTS = 20;
	
	/**
     * Main method that runs when the program is started.
     */
	public static void main(String[] args) {
		MailBox controllerQueries = new MailBox();
		MailBox clients[] = new MailBox[NUMBER_OF_CLIENTS]; // Clients' mailboxes
		
		// Create and launch the screen thread
		Screen screen = new Screen();
		screen.start();

		// Create and launch NUMBER_OF_CLIENTS Client threads
		for (int i = 0; i < NUMBER_OF_CLIENTS; i++) {
			clients[i] = new MailBox(1);
			Client c = new Client(i, clients[i], controllerQueries);
			c.start();
		}
		
		// Create and launch the Server Controller
		Controller controller = new Controller(controllerQueries, clients);
		controller.start();

	}
}
