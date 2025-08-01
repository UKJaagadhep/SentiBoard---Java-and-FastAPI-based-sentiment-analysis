import java.text.SimpleDateFormat;
import java.util.Date;

public class Feedback {
    String text, sentiment, user;
    Date timestamp;

    public Feedback(String text, String sentiment, String user, Date timestamp) {
        this.text = text;
        this.sentiment = sentiment;
        this.user = user;
        this.timestamp = timestamp;
    }

    public String toString() {
        return user + " | " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp) + " | " + sentiment + " | " + text;
    }

    public String toCSV() {
        return user + "," + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp) + "," + sentiment + "," + text.replace(",", " ");
    }

    public String toJSON() {
        return String.format("{\"user\":\"%s\",\"timestamp\":\"%s\",\"sentiment\":\"%s\",\"text\":\"%s\"}",
                user, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(timestamp), sentiment, text.replace("\"", "\\\""));
    }

    public static Feedback fromCSV(String line) {
        String[] parts = line.split(",", 4);
        if (parts.length < 4) return null;
        try {
            Date ts = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(parts[1]);
            return new Feedback(parts[3], parts[2], parts[0], ts);
        } catch (Exception e) {
            return null;
        }
    }
}