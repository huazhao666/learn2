package atomic_demo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with Intellij IDEA
 * Description:
 * User : 花朝
 * Date : 2021-01-19
 * Time : 10:25
 */
public class UseLock {
    private static final int COUNT = 100000000;
    private static int ai = 0;

    public static void main(String[] args) throws InterruptedException {
        long b = System.currentTimeMillis();
        Thread adder = new Thread(() ->{
            for (int i = 0; i < COUNT; i++) {
                synchronized (UseLock.class){
                    ai++;
                }
            }
        });
        adder.start();
        Thread suber = new Thread(() ->{
            for (int i = 0; i < COUNT; i++) {
                synchronized (UseLock.class){
                    ai--;
                }
            }
        });
        suber.start();

        adder.join();
        suber.join();

        System.out.println(ai);
        long s = System.currentTimeMillis();
        System.out.println(s-b);
    }
}
