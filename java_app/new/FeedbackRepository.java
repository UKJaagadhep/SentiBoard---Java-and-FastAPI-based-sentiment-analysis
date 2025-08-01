import java.io.*;
import java.util.*;

public class FeedbackRepository {
    private static final String DB_FILE = "feedback_db.json";
    private final List<Feedback> feedbackList = new ArrayList<>();

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public void loadFeedback() {
        File file = new File(DB_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Feedback fb = Feedback.fromCSV(line);
                if (fb != null) feedbackList.add(fb);
            }
        } catch (IOException e) {
            System.out.println("Failed to load DB");
        }
    }

    public void saveFeedback() {
        try (PrintWriter pw = new PrintWriter(DB_FILE)) {
            for (Feedback fb : feedbackList) {
                pw.println(fb.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Failed to save DB");
        }
    }
}