package com.career.ai_mentor.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AIClientService {

	@Value("${ai.api.key}")
	private String apiKey;

    public String getAIResponse(String message) {
        try {

            String json = "{"
                    + "\"model\":\"gpt-4o-mini\","
                    + "\"messages\":[{\"role\":\"user\",\"content\":\"" + message.replace("\"", "\\\"") + "\"}]"
                    + "}";

            ProcessBuilder pb = new ProcessBuilder(
                    "curl",
                    "-X", "POST",
                    "https://api.openai.com/v1/chat/completions",
                    "-H", "Content-Type: application/json",
                    "-H", "Authorization: Bearer " + apiKey,
                    "-d", json
            );

            Process process = pb.start();

            java.io.InputStream is = process.getInputStream();
            String response = new String(is.readAllBytes());

            return extractReply(response);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error getting AI response";
        }
    }

    // 🔥 Extract only AI message
    private String extractReply(String json) {
        try {
            int start = json.indexOf("\"content\":\"") + 11;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        } catch (Exception e) {
            return "AI response parsing error";
        }
    }
}