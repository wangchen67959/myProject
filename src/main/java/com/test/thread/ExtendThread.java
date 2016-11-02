package com.test.thread;

public class ExtendThread extends Thread {
	private String name;
	private int count = 5;

	public ExtendThread(String name) {
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
			System.out.println(name + "运行：" + count--);
			try {
				sleep((int) Math.random()*10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ExtendThread th1 = new ExtendThread("A");
		ExtendThread th2 = new ExtendThread("B");
		th1.start();
		th2.start();
	}
}
