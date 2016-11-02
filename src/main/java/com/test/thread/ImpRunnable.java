package com.test.thread;

/**
 * 适合多个相同的程序代码的线程去处理同一个资源
 * 
 * 可以避免java中的单继承的限制
 * 
 * 增加程序的健壮性，代码可以被多个线程共享，代码和数据独立
 * 
 * @author wangchen20097
 *
 */
public class ImpRunnable implements Runnable {
	private String name;
	private int count;

	public ImpRunnable() {

	}

	public ImpRunnable(String name) {
		this.name = name;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
//			System.out.println(name +"运行" + i);
			System.out.println(Thread.currentThread().getName() + "运行： count =" + count--);
			try {
				Thread.sleep((int) Math.random() * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
//		Thread th1 = new Thread(new ImpRunnable("C"));
//		Thread th2 = new Thread(new ImpRunnable("D"));
//		th1.start();
//		th2.start();

		ImpRunnable th = new ImpRunnable();
		Thread t = new Thread(th, "C");
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
		new Thread(th, "D").start();
		new Thread(th, "E").start();
	}

}
