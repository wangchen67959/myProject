package com.test.thread;

public class RunnableImp {
	// 一个账户
	class Bank {
		private int count = 20;

		private Boolean isUse = false;

		public synchronized void save(int money) {
			if (isUse) {
				try {
					this.wait();
					System.out.println("save  "+count);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("save    " + isUse);
			count += money;
			isUse = true;
			this.notify();

		}

		public synchronized void jian(int money) {
			System.out.println("jian   " + isUse);
			if (!isUse) {
				try {
					this.wait();
					System.out.println("jian  "+count);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			count -= money;
			isUse = false;
			this.notify();
		}

	}

	class addRunnableThread implements Runnable {
		private Bank bank;

		public addRunnableThread(Bank bank) {
			this.bank = bank;
		}

		public void run() {
			for (int i = 0; i < 2; i++) {
				bank.save(20);
				System.out.println(Thread.currentThread().getName() + "操作后余额为"
						+ bank.count);
			}
		}
	}

	class jianRunnableThread implements Runnable {
		private Bank bank;

		public jianRunnableThread(Bank bank) {
			this.bank = bank;
		}

		public void run() {
			for (int i = 0; i < 2; i++) {
				bank.jian(2);
				System.out.println(Thread.currentThread().getName() + "操作后余额为"
						+ bank.count);
			}
		}
	}

	public void createThead() {
		// 账户必须为同一个, 可造成死锁
		Bank bank = new Bank();
		addRunnableThread runnableThread = new addRunnableThread(bank);
		Thread th = new Thread(runnableThread, "A");
		th.start();

		jianRunnableThread runnableThread2 = new jianRunnableThread(bank);
		Thread th2 = new Thread(runnableThread2, "B");
		th2.start();
	}

	public static void main(String[] args) {
		RunnableImp runnableImp = new RunnableImp();
		runnableImp.createThead();
	}

}
