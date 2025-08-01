import java.io.*;
import java.net.*;
import java.util.Date;

public class SentimentService {
    public Feedback analyzeSentiment(String text, String user) throws IOException {
        String jsonInput = "{\"text\": \"" + text.replace("\"", "\\\"") + "\"}";
        URL url = new URL("http://127.0.0.1:8000/analyze");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes("utf-8"));
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        String json = response.toString();
        String sentiment = "";
        int idx = json.indexOf("\"emotion\"");
        if (idx != -1) {
            int start = json.indexOf(":", idx) + 1;
            int quote1 = json.indexOf("\"", start);
            int quote2 = json.indexOf("\"", quote1 + 1);
            if (quote1 != -1 && quote2 != -1) {
                sentiment = json.substring(quote1 + 1, quote2);
            }
        }

        return new Feedback(text, sentiment, user, new Date());
    }
}