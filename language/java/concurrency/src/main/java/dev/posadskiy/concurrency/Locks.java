package dev.posadskiy.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Locks {

	private static Lock lock = new ReentrantLock();

	public static void main(String[] args) {

		Runnable runnableOne = () -> {
			System.out.println("[1] started");
			lock.lock();
			System.out.println("[1] Want to sleep...");
			try {
				Thread.sleep(10000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("[1] Wake up!");
			lock.unlock();
			System.out.println("[1] Ended");
		};
		Runnable runnableTwo = () -> {
			System.out.println("[2] started");
			lock.lock();
			System.out.println("[2] Want to sleep...");
			try {
				Thread.sleep(10000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("[2] Wake up!");
			lock.unlock();
			System.out.println("[2] Ended");
		};

		Thread threadOne = new Thread(runnableOne);
		Thread threadTwo = new Thread(runnableTwo);

		threadOne.start();
		threadTwo.start();

	}
}
