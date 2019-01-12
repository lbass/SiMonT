import com.lbass.simont.monitor.MonitorServer;
import com.lbass.simont.monitor.MonitorServer.ServerMonitorMetric;
import com.lbass.simont.slack.SlackSendData.ALERT_LEVEL;
import com.lbass.simont.slack.SlackSender;
import org.junit.Test;

public class SlackSenderTest {

    @Test
    public void test() {
        ServerMonitorMetric serverMonitorMetric = MonitorServer.collect();
        SlackSender.sendSlack(serverMonitorMetric.toString(), ALERT_LEVEL.WARN);
    }
}
