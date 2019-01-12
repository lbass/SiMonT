import com.lbass.simont.monitor.MonitorServer;
import com.lbass.simont.monitor.MonitorServer.ServerMonitorMetric;
import org.junit.Test;

public class ServerMonitorTest {

    @Test
    public void test () {
        ServerMonitorMetric metric = MonitorServer.collect();
        System.out.println(metric);
    }
}
