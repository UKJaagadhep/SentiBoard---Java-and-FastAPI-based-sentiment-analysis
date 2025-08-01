import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class FeedbackManager {
    private final FeedbackRepository feedbackRepo;
    private final SentimentService sentimentService;

    public FeedbackManager(FeedbackRepository repo, SentimentService service) {
        this.feedbackRepo = repo;
        this.sentimentService = service;
    }

    public void dashboard(Scanner scanner, String loggedInUser, ReportExporter reportExporter) throws IOException {
        while (true) {
            System.out.println("\n1. Upload feedback (CSV)\n2. Enter new feedback manually\n3. View sentiment reports\n4. View trend chart\n5. Export report\n6. Logout");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> uploadCSV(scanner, loggedInUser);
                case 2 -> manualEntry(scanner, loggedInUser);
                case 3 -> viewReports();
                case 4 -> viewTrends();
                case 5 -> reportExporter.exportReport(scanner, feedbackRepo.getFeedbackList(), loggedInUser);
                case 6 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void uploadCSV(Scanner scanner, String loggedInUser) throws IOException {
        System.out.print("Enter CSV file path: ");
        String path = scanner.nextLine();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<Feedback>> futures = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String text = line.trim();
                if (!text.isEmpty()) {
                    futures.add(executor.submit(() -> sentimentService.analyzeSentiment(text, loggedInUser)));
                }
            }
        }

        for (Future<Feedback> f : futures) {
            try {
                Feedback fb = f.get();
                feedbackRepo.getFeedbackList().add(fb);
            } catch (Exception e) {
                System.out.println("Error analyzing sentiment");
            }
        }
        executor.shutdown();
        feedbackRepo.saveFeedback();
        System.out.println("CSV upload and sentiment analysis completed.");
    }

    private void manualEntry(Scanner scanner, String loggedInUser) throws IOException {
        System.out.print("Enter text: ");
        String text = scanner.nextLine();
        Feedback fb = sentimentService.analyzeSentiment(text, loggedInUser);
        feedbackRepo.getFeedbackList().add(fb);
        feedbackRepo.saveFeedback();
        System.out.println("Sentiment Result: " + fb.sentiment);
    }

    private void viewReports() {
        System.out.println("Feedback History:");
        for (Feedback fb : feedbackRepo.getFeedbackList()) {
            System.out.println(fb);
        }
    }

    private void viewTrends() {
        Map<String, Integer> sentimentCount = new HashMap<>();
        for (Feedback fb : feedbackRepo.getFeedbackList()) {
            sentimentCount.put(fb.sentiment, sentimentCount.getOrDefault(fb.sentiment, 0) + 1);
        }
        System.out.println("Sentiment Trends:");
        for (var entry : sentimentCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}