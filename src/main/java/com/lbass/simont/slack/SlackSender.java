package com.lbass.simont.slack;

import com.google.gson.Gson;
import com.lbass.simont.slack.SlackSendData.ALERT_LEVEL;
import com.lbass.simont.slack.SlackSendData.Attachment;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class SlackSender {

	private final static String WebHookUrl = "https://hooks.slack.com/services/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";
	
	public static String sendSlack(String message, ALERT_LEVEL level) {
		String result = null;
		try {
			SlackSendData sendData = new SlackSendData();
			Attachment attachment = new Attachment();
			attachment.setText(message);
			attachment.setColor(level.getColor());

			sendData.setAttachments(Arrays.asList(attachment));

			String jsonString = new Gson().toJson(sendData);

			log.debug("[Send Slack]");
			log.debug("Send Data : " + jsonString);

			result = Jsoup.connect(WebHookUrl)
					.ignoreContentType(true)
					.requestBody(jsonString)
					.post()
					.body()
					.toString();

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
}