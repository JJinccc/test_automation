package tests.stress;

import com.ffan.qa.utils.SystemUtil;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CouponQueueStressTests extends TestBase {
    @Test
    public void couponStress() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    SystemUtil.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "finished.");
                }
            };
            executorService.execute(syncRunnable);
        }

        SystemUtil.sleep(500 * 1000);
    }
}
