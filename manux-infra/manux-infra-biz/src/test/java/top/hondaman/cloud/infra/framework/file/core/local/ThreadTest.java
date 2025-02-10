package top.hondaman.cloud.infra.framework.file.core.local;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ThreadTest {
    class MyThread extends Thread{
        private String threadName;
        public MyThread(String threadName){
            this.threadName = threadName;
        }
        @Override
        public void run() {
            for (int i = 1;i<=10;i++){
                System.out.println(String.format("执行线程[%s]任务[%s]",this.threadName,i));
            }
        }
    }
    @Test
    public void threadTest(){
        new MyThread("A").start();
        new MyThread("B").start();
        new MyThread("C").start();
        new MyThread("D").start();
    }


    class RunnalbleTask implements Runnable{
        private String threadName;
        public RunnalbleTask(String threadName){
            this.threadName = threadName;
        }
        @Override
        public void run() {
            for (int i = 1;i<=10;i++){
                System.out.println(String.format("执行线程[%s]任务[%s]",this.threadName,i));
            }
        }
    }
    @Test
    public void runnableTest(){
        new Thread(new RunnalbleTask("A")).start();
        new Thread(new RunnalbleTask("B")).start();
        new Thread(new RunnalbleTask("C")).start();
    }

    class CallableTask implements Callable<Integer>{
        @Override
        public Integer call() throws Exception {
            return 200;
        }
    }
    @Test
    public void callableTest() throws Exception{
        FutureTask<Integer> futureTask = new FutureTask<>(new CallableTask());
        new Thread(futureTask,"计算线程").start();
        System.out.println(futureTask.get());
    }

    @Test
    public void threadPoolExecutorTest(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, //核心线程数5
                10,  //最大线程数10
                1L,  //等待时间为1L
                TimeUnit.SECONDS,  //等待时间的单位为秒
                new ArrayBlockingQueue<>(100),  //任务队列为ArrayBlockingQueue，容量为100
                new ThreadPoolExecutor.CallerRunsPolicy()  //拒绝策略为CallerRunsPolicy
        );
        threadPoolExecutor.execute(new RunnalbleTask("A"));
        threadPoolExecutor.execute(new RunnalbleTask("B"));
        threadPoolExecutor.execute(new RunnalbleTask("C"));

        threadPoolExecutor.shutdown();  //终止线程池
    }
}
