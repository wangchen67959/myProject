package com.test.thread;

public class ExtendThread extends Thread {
	private String name;
	private int count = 5;
	private String lockStatus = "money";

	public ExtendThread(String name) {
		this.name = name;
	}

	public void save(int many) {
		synchronized ("a") {
			count += many;
		}
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
			save(2);
			System.out.println(name + "操作后账户余额为：" + count);
		}
	}

	public static void main(String[] args) {
		// 开了两个不同的线程，资源不共享
		ExtendThread th1 = new ExtendThread("A");
		ExtendThread th2 = new ExtendThread("B");
		th1.start();
		th2.start();
	}
}
