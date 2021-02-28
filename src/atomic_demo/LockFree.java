package atomic_demo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with Intellij IDEA
 * Description:
 * User : 花朝
 * Date : 2021-01-19
 * Time : 10:16
 */
public class LockFree {
    private static final int COUNT = 100000000;
    private static final AtomicInteger ai = new AtomicInteger(0);//原子类；

    public static void main(String[] args) throws InterruptedException {
        long b = System.currentTimeMillis();
        Thread adder = new Thread(() ->{
            for (int i = 0; i < COUNT; i++) {
                ai.getAndIncrement();//++
            }
        });
        adder.start();
        Thread suber = new Thread(() ->{
            for (int i = 0; i < COUNT; i++) {
                ai.getAndDecrement(); //--
            }
        });
        suber.start();

        adder.join();
        suber.join();

        System.out.println(ai.get());
        long s = System.currentTimeMillis();
        System.out.println(s-b);
    }
}
