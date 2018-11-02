package top.lshaci.framework.thread.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread pool executor utils
 * 
 * @author lshaci
 * @since 0.0.4
 */
public class ExecutorUtils {
    
    /**
     * The available processor number
     */
    public final static int PROCESSORS = Runtime.getRuntime().availableProcessors();
    
    /**
     * The fixed number of thread pools
     */
    private static ExecutorService threadPool = Executors.newFixedThreadPool(PROCESSORS * 2);
    
    /**
     * Use the thread pool to perform tasks
     * 
     * @param exec the commands to be executed
     */
    public static void execute(Runnable exec) {
        threadPool.execute(exec);
    }

}
