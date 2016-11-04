package com.test.thread;

public class TestBack {
	public static void main(String[] args) {

		final DepositOrWithdrawBusiness business = new DepositOrWithdrawBusiness(
				1000);
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				public void run() {
					business.deposit(300);
				}
			}).start();
			new Thread(new Runnable() {
				public void run() {
					business.withdraw(300);
				}
			}).start();
		}
	}
}

class DepositOrWithdrawBusiness {
	int balance = 1000;

	public DepositOrWithdrawBusiness(int balance) {
		this.balance = balance;
	}

	boolean isWithdraw = false;

	public synchronized void deposit(int m) {
		if (isWithdraw) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		balance = balance + m;
		System.out.println(Thread.currentThread().getName()
				+ " deposit $300,now balance:" + balance);
		isWithdraw = true;
		this.notify();

	}

	public synchronized void withdraw(int m) {
		if (!isWithdraw) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		balance = balance - m;
		System.out.println(Thread.currentThread().getName()
				+ " withdraw $300,now balance:" + balance);
		isWithdraw = false;
		this.notify();
	}
}