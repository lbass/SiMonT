package com.lbass.simont.monitor;

import com.sun.management.OperatingSystemMXBean;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MonitorServer {

	private static final Logger logger = LoggerFactory.getLogger(MonitorServer.class);

	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final String HOST_NAME;

	static {
		String tempHostName;
		try {
			tempHostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			tempHostName = "unknown";
			logger.error("Can not get host name");
		}
		HOST_NAME = tempHostName;
	}

	public static ServerMonitorMetric collect() {
		ServerMonitorMetric serverMonitorMetric = new ServerMonitorMetric();
		int mb = 1024 * 1024;
		OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// CPU
		BigDecimal cpuLoad = new BigDecimal(os.getProcessCpuLoad());
		logger.debug("server cpu load: {}", os.getProcessCpuLoad());
		serverMonitorMetric.setCpuLoad(cpuLoad.multiply(BigDecimal.valueOf(100)).setScale(1, 1));

		// MEMORY
		long physicalMemorySize = os.getTotalPhysicalMemorySize();
		long physicalFreeMemorySize = os.getFreePhysicalMemorySize();
		BigDecimal totalUsedMemory = BigDecimal.valueOf(physicalMemorySize - physicalFreeMemorySize)
				.divide(BigDecimal.valueOf(physicalMemorySize), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);
		BigDecimal totalFreeMemory = BigDecimal.valueOf(physicalFreeMemorySize)
				.divide(BigDecimal.valueOf(physicalMemorySize), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);

		serverMonitorMetric.setPhysicalMemorySize(BigDecimal.valueOf(physicalMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP));
		serverMonitorMetric.setPhysicalFreeMemorySize(BigDecimal.valueOf(physicalFreeMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP));
		serverMonitorMetric.setTotalUsedPer(totalUsedMemory);
		serverMonitorMetric.setTotalFreePer(totalFreeMemory);

		return serverMonitorMetric;
	}

	@Data
	public static class ServerMonitorMetric {
		private BigDecimal cpuLoad;
		private BigDecimal physicalMemorySize;
		private BigDecimal physicalFreeMemorySize;
		private BigDecimal totalUsedPer;
		private BigDecimal totalFreePer;
		private String warnMessage;

		@Override
		public String toString() {
			return "==== [" + HOST_NAME + "] Server State ====" +
					"\n " + warnMessage +
					"\n cpuLoad: " + cpuLoad + "%" +
					"\n physicalMemorySize: " + physicalMemorySize +
					"\n physicalFreeMemorySize: " + physicalFreeMemorySize +
					"\n totalUsedPer: " + totalUsedPer + "%" +
					"\n totalFreePer: " + totalFreePer + "%";
		}
	}
}
