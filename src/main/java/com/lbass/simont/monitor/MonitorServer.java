package com.lbass.simont.monitor;

import com.sun.management.OperatingSystemMXBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class MonitorServer {

	private static final Runtime RUNTIME = Runtime.getRuntime();
	private static final String HOST_NAME;

	static {
		String tempHostName;
		try {
			tempHostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			tempHostName = "unknown";
			log.error("Can not get host name");
		}
		HOST_NAME = tempHostName;
	}

	public static ServerMonitorMetric collect() {
		ServerMonitorMetric serverMonitorMetric = new ServerMonitorMetric();
		int mb = 1024 * 1024;
		OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		// CPU
		BigDecimal cpuLoad = new BigDecimal(os.getProcessCpuLoad());
		log.info("server cpu load: {}", os.getProcessCpuLoad());
		serverMonitorMetric.setCpuLoad(cpuLoad.multiply(BigDecimal.valueOf(100)).setScale(1, 1));

		// MEMORY
		long physicalMemorySize = os.getTotalPhysicalMemorySize();
		long physicalFreeMemorySize = os.getFreePhysicalMemorySize();
		// 실제 사용 중인 메모리
		long committedVirtualMemorySize = os.getCommittedVirtualMemorySize();

		BigDecimal physicalMemorySizeMb = BigDecimal.valueOf(physicalMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal physicalFreeMemorySizeMb = BigDecimal.valueOf(physicalFreeMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP);
		BigDecimal committedVirtualMemorySizeMb = BigDecimal.valueOf(committedVirtualMemorySize).divide(BigDecimal.valueOf(mb), 2, BigDecimal.ROUND_HALF_UP);

		BigDecimal usedMemory = BigDecimal.valueOf(committedVirtualMemorySize)
				.divide(BigDecimal.valueOf(physicalMemorySize), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);

		BigDecimal freeMemory = BigDecimal.valueOf(physicalMemorySize - committedVirtualMemorySize)
				.divide(BigDecimal.valueOf(physicalMemorySize), 2, BigDecimal.ROUND_HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(1, 1);

		log.info("physicalMemorySize: {}", physicalMemorySizeMb);
		log.info("physicalFreeMemorySize: {}", physicalFreeMemorySizeMb);
		log.info("committedVirtualMemorySize: {}", committedVirtualMemorySizeMb);

		serverMonitorMetric.setPhysicalMemorySize(physicalMemorySizeMb);
		serverMonitorMetric.setPhysicalFreeMemorySize(physicalFreeMemorySizeMb);
		serverMonitorMetric.setCommittedVirtualMemorySize(committedVirtualMemorySizeMb);
		serverMonitorMetric.setCommittedVirtualMemorySize(committedVirtualMemorySizeMb);
		serverMonitorMetric.setUsedMemoryPer(usedMemory);
		serverMonitorMetric.setFreeMemoryPer(freeMemory);

		return serverMonitorMetric;
	}

	@Data
	public static class ServerMonitorMetric {
		private BigDecimal cpuLoad;
		private BigDecimal physicalMemorySize;
		private BigDecimal physicalFreeMemorySize;
		private BigDecimal committedVirtualMemorySize;
		private BigDecimal realFreeMemorySize;
		private BigDecimal usedMemoryPer;
		private BigDecimal freeMemoryPer;
		private String warnMessage = "";

		@Override
		public String toString() {
			return "\n==== [" + HOST_NAME + "] Server State ====" +
					"\n " + warnMessage +
					"\n cpuLoad: " + cpuLoad + "%" +
					"\n physicalMemorySize: " + physicalMemorySize +
					"\n physicalFreeMemorySize: " + physicalFreeMemorySize +
					"\n committedVirtualMemorySize: " + committedVirtualMemorySize +
					"\n usedMemoryPer: " + usedMemoryPer + "%" +
					"\n freeMemoryPer: " + freeMemoryPer + "%";
		}
	}
}
