package countDown_latch;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int N = 100;
        final long[][] arrays = new long[N][];


        for (int i = 0; i < N; i++) {
            arrays[i] = new long[100_0000];
        }

        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; i++) {
            final long[] targetArray = arrays[i];
            Thread worker = new Thread(() -> {
                try {
                    // 做一些任务的准备工作
                    // 还没有收到开始信号，等待
                    startSignal.await();
                    // 收到了开始信号了，开始执行任务

                    Random random = new Random();
                    TimeUnit.SECONDS.sleep(random.nextInt(10) + 10); //主线程随机休眠几秒
                    Arrays.sort(targetArray);//子线程要做的工作
                    Thread thread = Thread.currentThread(); // 返回当前线程对象的引用
                    System.out.printf("%s: 已经完成任务\n", thread.getName());

                    // 任务执行完成，但还没有汇报
                    doneSignal.countDown();
                    // 任务执行完成，并且已经汇报
                } catch (InterruptedException e) {
                    // 有人让该线程中断
                    e.printStackTrace();
                }
            }, String.format("工作线程-%02d", i));
            worker.start();
        }

        // 特工已经埋伏下去了，没有给开始信号，特工不会开始任务
        Random r = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 100_0000; j++) {
                arrays[i][j] = r.nextInt();
            }
        }
        // 所以可以做一些准备工作
        startSignal.countDown();    // 发出开始指令，表示各个特工可以开始执行任务了
        // 能不能说，每个特工都已经开始执行任务了？ —— 不能

        // 等待所有特工都完成任务之后，进行情况汇总
        doneSignal.await();     // 一共有 N 个信号，所以，只有 N 个结束信号都收到时，表示所有任务都完成了
        // 肯定所有任务都完成了
        // 进行汇总工作
        for (int i = 0; i < N; i++) {
            // 肯定数组都排好序了
            // 肯定没有报错
            if (arrays[i][0] > arrays[i][100_0000 - 1]) {
                System.out.println("错误了");
            }
        }
    }
}
