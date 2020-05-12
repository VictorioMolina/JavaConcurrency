package Exercise3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TernarySequencer {
	private ReentrantLock l = new ReentrantLock();
	private int nFirstOperationsCompleted;
	private int nSecondOperationsCompleted;
	private int nThirdOperationsCompleted;
	private Condition firstOperation = l.newCondition();
	private Condition secondOperation = l.newCondition();
	private Condition thirdOperation = l.newCondition();

	public TernarySequencer() {
		nFirstOperationsCompleted = 0;
		nSecondOperationsCompleted = 0;
		nThirdOperationsCompleted = 0;
	}

	public void operation1(int id) throws InterruptedException {
		l.lock();
		try {
			while (nFirstOperationsCompleted > nThirdOperationsCompleted)
				firstOperation.await();

			nFirstOperationsCompleted++;
			System.out.println(String.format("%s\n%s\n%s\n\n", "Thread " + id + " -----", "Executes operation 1",
					"End of Thread " + id + " -----"));

			secondOperation.signalAll();
		} finally {
			l.unlock();
		}
	}

	public void operation2(int id) throws InterruptedException {
		l.lock();
		try {
			while (nSecondOperationsCompleted >= nFirstOperationsCompleted)
				secondOperation.await();

			nSecondOperationsCompleted++;
			System.out.println(String.format("%s\n%s\n%s\n\n", "Thread " + id + " -----", "Executes operation 2",
					"End of Thread " + id + " -----"));

			thirdOperation.signalAll();
		} finally {
			l.unlock();
		}
	}

	public void operation3(int id) throws InterruptedException {
		l.lock();
		try {
			while (nThirdOperationsCompleted >= nSecondOperationsCompleted)
				thirdOperation.await();

			nThirdOperationsCompleted++;
			System.out.println(String.format("%s\n%s\n%s\n\n", "Thread " + id + " -----", "Executes operation 3",
					"End of Thread " + id + " -----"));

			firstOperation.signalAll();
		} finally {
			l.unlock();
		}
	}
}
