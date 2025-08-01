import java.util.Scanner;
public class SentimentClient {
    public static void main(String[] args) throws Exception {
        UserManager userManager = new UserManager();
        FeedbackRepository feedbackRepo = new FeedbackRepository();
        SentimentService sentimentService = new SentimentService();
        ReportExporter reportExporter = new ReportExporter();
        FeedbackManager feedbackManager = new FeedbackManager(feedbackRepo, sentimentService);

        userManager.seedUsers();
        feedbackRepo.loadFeedback();

        Scanner scanner = new Scanner(System.in);
        String loggedInUser = null;

        System.out.println("Welcome to SentiBoard!");
        while (true) {
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    loggedInUser = userManager.login(scanner);
                    if (loggedInUser != null) {
                        feedbackManager.dashboard(scanner, loggedInUser, reportExporter);
                    }
                }
                case 2 -> userManager.register(scanner);
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid choice");
            }
        }
    }
}