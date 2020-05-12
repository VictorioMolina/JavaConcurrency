package Exercise1;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


// Class in charge of accumulating the sum of the pairs
class PairsAdder extends Thread {
	private static final int ANALYZED_LIMIT_COUNT = 250;
	
	private int id;
	private int [] buffer;
	private SharedVariable totalSum;
	private int parcialSum;
	private int analyzedCounter;
	private int bufferPointer;
	
	public PairsAdder(int id, int [] buffer, SharedVariable totalSum) {
		this.id = id;
		this.buffer = buffer;
		this.totalSum = totalSum;
		parcialSum = 0;
		analyzedCounter = 0;
		bufferPointer = id * 250;
	}
	
	public void run() {
		while (analyzedCounter < ANALYZED_LIMIT_COUNT) {
			// Setting the  number of elements to analyze
			int analyzeNumber = new Random(System.currentTimeMillis())
									.nextInt(ANALYZED_LIMIT_COUNT) + 1;
			
			while (analyzedCounter < analyzeNumber) {
				// If the element is pair
				if(buffer[bufferPointer] % 2 == 0) {
					// Recalculate the parcial sum
					parcialSum += buffer[bufferPointer];
				}

				// Increase the number of analyzed elements
				analyzedCounter++;
				
				// Increase the buffer pointer
				bufferPointer++;
			}
		}
		
		// Increasing the total sum
		totalSum.increase(parcialSum);
		
		// Show the result on screen
		String result = String.format("%s\n%s\n%s\n"
				,"Thread " + id + " -----"
				, "Parcial sum = " + parcialSum
			    , "End thread " + id + " -----"
		);
		
		Screen.print(result);
	}
}


// Class for creating incremental shared variables
class SharedVariable {
	private final Lock l = new ReentrantLock();
	
	private int value;
	
	public SharedVariable(int value) {
		this.value = value;
	}
	
	public void increase(int increment){
		 int temp;

		 l.lock();
		 try{
			 temp = value;
			 try {
				 Thread.sleep((int) Math.round(Math.random()));
			 } catch (InterruptedException e) {
				 e.printStackTrace();
			 }
			 temp += increment;
			 value = temp;
		 } finally{
			 l.unlock();
		 }
	} 
	
	public int getValue(){
		return value;
	}
}


// Class that represents the screen
class Screen {
	private static final Lock l = new ReentrantLock();
	
	public static void print(String str) {
		l.lock();
		System.out.println(str);
		l.unlock();
	}
}


// Main class
public class AddPairs {
	/**
     * Main method that runs when the program is started.
     */
	public static void main(String[] args) {
		// Generating the random buffer
		int [] buffer = new Random().ints(5000, 0, 10).toArray(); // 5000 elements from 0 to 10
		
		// Printing the buffer with a good visual format
		System.out.println("Array -----");
		int sum = 0;
		for (int i = 0; i < buffer.length; i++) {
			if (buffer[i] % 2 == 0) 
				sum += buffer[i];
			
			System.out.print(buffer[i] + " ");
			if ((i+1) % 20 == 0)
				System.out.println();
		}
		System.out.println("Total sum of pairs: " + sum);
		System.out.println();
		
		// Creating the shared variable
		SharedVariable totalSum = new SharedVariable(0);
		
		// Creating and starting 20 Threads of PairsAdders
		PairsAdder[] pairsAdders = new PairsAdder[20];
		for (int i = 0; i < pairsAdders.length; i++) {
	        pairsAdders[i] = new PairsAdder(i, buffer, totalSum);
	        pairsAdders[i].start();
	    }
		
		// Wait all threads to finish
		for (PairsAdder pairsAdder : pairsAdders) {
			try {
				pairsAdder.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Printing the total sum os pairs calculated by our threads
		System.out.println("Calculated total sum of pairs: " + totalSum.getValue());
	}
}
