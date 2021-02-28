package file_scan;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with Intellij IDEA
 * Description:
 * User : 花朝
 * Date : 2021-01-19
 * Time : 9:14
 */
public class ScanTask implements Runnable {
    private final File directory;
    private final ExecutorService threadPool;
    private final AtomicInteger counter;
    private final CountDownLatch done;
    public ScanTask(File directory, ExecutorService threadPool, AtomicInteger counter,CountDownLatch done){
        this.directory = directory;
        this.threadPool = threadPool;
        this.counter = counter;
        this.done = done;
    }
    @Override
    public void run() {
        //counter.incrementAndGet();//先加在得到
        File[] files = directory.listFiles();
        if(files != null){
            for (File file : files) {
                if(file.isDirectory()){
                    ScanTask task = new ScanTask(file,threadPool,counter,done);
                    counter.incrementAndGet();//++；
                    threadPool.execute(task);//x线程安全
                }
            }
        }
        if(counter.decrementAndGet() == 0){ //先减在得到
            done.countDown();
        }
    }
}
