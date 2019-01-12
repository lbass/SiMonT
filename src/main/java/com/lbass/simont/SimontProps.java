package com.lbass.simont;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class SimontProps {

    private final static String FILE_NAME = "simontConfig.properties";
    private Properties properties;

    public SimontProps() {
        properties = new Properties();
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(FILE_NAME);
            properties.load(inputStream);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadProps() {
        properties = new Properties();
        try {
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(FILE_NAME);
            properties.load(inputStream);
            inputStream.close();

            log.debug("CpuThreshold {}", getCpuThreshold());
            log.debug("MemoryThreshold {}", getMemoryThreshold());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getCpuThreshold() {
        String threshold = properties.getProperty("simont.cpu.threshold");
        Integer thresholdValue = null;

        try {
            if(threshold != null && !"".equalsIgnoreCase(threshold)) {
                thresholdValue = Integer.parseInt(threshold);
            }
        } catch (Exception e) {
            log.warn("Invalid CpuThreshold: {}", threshold);
        }

        if(threshold == null) {
            thresholdValue = 50;
        }

        return thresholdValue;
    }

    public int getMemoryThreshold() {
        String threshold = properties.getProperty("simont.memory.threshold");
        Integer thresholdValue = null;

        try {
            if(threshold != null && !"".equalsIgnoreCase(threshold)) {
                thresholdValue = Integer.parseInt(threshold);
            }
        } catch (Exception e) {
            log.warn("Invalid MemotyThreshold: {}", threshold);
        }

        if(threshold == null) {
            thresholdValue = 50;
        }

        return thresholdValue;
    }

    public boolean isNotifySlack() {
        String notifyConfig = properties.getProperty("slack.notify");
        Boolean isNotify = null;

        try {
            if(notifyConfig != null && !"".equalsIgnoreCase(notifyConfig)) {
                isNotify = Boolean.valueOf(notifyConfig);
            }
        } catch (Exception e) {
            log.warn("Invalid Slack Notify Config: {}", notifyConfig);
        }

        if(isNotify == null) {
            isNotify = true;
        }

        return isNotify;
    }
}
