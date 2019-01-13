package com.lbass.simont;

import com.lbass.simont.helper.ThreadHelper;
import com.lbass.simont.monitor.MonitorServer;
import com.lbass.simont.monitor.MonitorServer.ServerMonitorMetric;
import com.lbass.simont.slack.SlackSendData.ALERT_LEVEL;
import com.lbass.simont.slack.SlackSender;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimontApplication {

    private SimontProps simontProps;

    public static void main(String[] args) {

        final SimontProps simontProps = new SimontProps();

        ScheduledExecutorService monitorScheduledThreadPool =
                ThreadHelper.getScheduledExecutorService("SiMonT Monitor" + "-%d", 1);

        monitorScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerMonitorMetric serverMonitorMetric = MonitorServer.collect();
                    int cpuThreshold = simontProps.getCpuThreshold();
                    int memoryThreshold = simontProps.getMemoryThreshold();
                    boolean isNotify = simontProps.isNotifySlack();
                    boolean isWarn = false;
                    if(serverMonitorMetric.getCpuLoad().longValue() > cpuThreshold) {
                        String warnMessage = String.format("CPU 임계치[%d] 초과 : %d",
                                cpuThreshold,
                                serverMonitorMetric.getCpuLoad().longValue());
                        serverMonitorMetric.setWarnMessage(warnMessage);
                        log.warn(warnMessage);
                        isWarn = true;
                    }
                    if(!isWarn && serverMonitorMetric.getUsedMemoryPer().longValue() > memoryThreshold) {
                        String warnMessage = String.format("MEMORY 임계치[%d] 초과 : %d",
                                memoryThreshold,
                                serverMonitorMetric.getFreeMemoryPer().longValue());
                        serverMonitorMetric.setWarnMessage(warnMessage);
                        log.warn(warnMessage);
                        isWarn = true;
                    }
                    if(isWarn && isNotify) {
                        SlackSender.sendSlack(serverMonitorMetric.toString(), ALERT_LEVEL.WARN);
                    } else {
                        log.info("monitoring {}", serverMonitorMetric);
                    }
                } catch (Exception e) {
                    log.error("{}", e);
                }

            }
        }, 60, 60, TimeUnit.SECONDS);

        ScheduledExecutorService propsScheduledThreadPool =
                ThreadHelper.getScheduledExecutorService("SiMonT Properties" + "-%d", 1);
        propsScheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    simontProps.reloadProps();
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }
        }, 0, 30, TimeUnit.SECONDS);

        while(true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
