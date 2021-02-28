package file_scan;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with Intellij IDEA
 * Description:
 * User : 花朝
 * Date : 2021-01-19
 * Time : 9:09
 */
public class Main {
    public static void main (String[] args) throws InterruptedException {
        File root = new File("D:\\");
        long b = System.currentTimeMillis();
        scanDirectory(root);
        long c = System.currentTimeMillis();
        System.out.println("root下的目录扫描完成");
        System.out.println(c-b);


    }
    private static void scanDirectory(File root) throws InterruptedException {
        ExecutorService threadPool = new ThreadPoolExecutor(10,10,
                0, TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        ExecutorService executor1 = Executors.newCachedThreadPool();
      /*  0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());*/
        Executors.newFixedThreadPool(4);
        /*nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());*/


        AtomicInteger counter = new AtomicInteger(0);//打卡器，并且线程安全
        CountDownLatch done = new CountDownLatch(1); //用来通知得
        ScanTask sc = new ScanTask(root,threadPool,counter,done); //准备工作已经作为

        //打卡
        counter.incrementAndGet(); // 领导这里打卡
        threadPool.execute(sc); //运行线程池

        // TODO: 想办法，在这里等，直到 root 整棵树都被扫描完成
        done.await(); //都做完了
        threadPool.shutdown();//关闭线程池
    }
}
