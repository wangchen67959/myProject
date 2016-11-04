package com.test.thread;

public class Thead3 {
	class Bank {
		 
        private int account = 100;

        public int getAccount() {
        	synchronized (this) {
        		return account;
			}
        }

        /**
         * 用同步方法实现
         * 
         * @param money
         */
        /**
         * @param money
         */
        public synchronized void save(int money) {
            account += money;
        }

        /**
         * 用同步代码块实现
         * 
         * @param money
         */
        public void save1(int money) {
            synchronized (this) {
                account += money;
            }
        }
    }

    class NewThread implements Runnable {
        private Bank bank;

        public NewThread(Bank bank) {
            this.bank = bank;
        }

        public void run() {
            for (int i = 0; i < 10; i++) {
                bank.save1(10);
                System.out.println(Thread.currentThread().getName() + "账户余额为：" + bank.getAccount());
            }
        }

    }

    /**
     * 建立线程，调用内部类
     */
    public void useThread() {
    	// 由实现runnable接口 创建同一个， 资源共享
        Bank bank = new Bank();
        NewThread new_thread = new NewThread(bank);
        System.out.println("线程1");
        Thread thread1 = new Thread(new_thread, "a");
        thread1.start();
        System.out.println("线程2");
        Thread thread2 = new Thread(new_thread, "b");
        thread2.start();
    }

    public static void main(String[] args) {
        Thead3 st = new Thead3();
        st.useThread();
    }
}
