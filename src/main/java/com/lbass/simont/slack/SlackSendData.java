package com.lbass.simont.slack;

import lombok.Data;

import java.util.List;

@Data
public class SlackSendData {

	private List<Attachment> attachments;

	public enum ALERT_LEVEL {
		GOOD("good"),
		WARN("warning"),
		DANGER("danger");

		private String color;

		private ALERT_LEVEL(String color) {
			this.color = color;
		}

		public String getColor() {
			return color;
		}
	}

	@Data
	public static class Attachment {
		private String text;
		private String color;
	}
}