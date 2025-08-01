import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ReportExporter {
    public void exportReport(Scanner scanner, List<Feedback> feedbackList, String username) throws IOException {
        System.out.print("Export as (csv/txt/json): ");
        String format = scanner.nextLine();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String baseName = "report_" + username + "_" + timestamp;
        switch (format) {
            case "csv" -> {
                try (PrintWriter pw = new PrintWriter(baseName + ".csv")) {
                    for (Feedback fb : feedbackList) {
                        pw.println(fb.toCSV());
                    }
                }
            }
            case "txt" -> {
                try (PrintWriter pw = new PrintWriter(baseName + ".txt")) {
                    for (Feedback fb : feedbackList) {
                        pw.println(fb);
                    }
                }
            }
            case "json" -> {
                try (PrintWriter pw = new PrintWriter(baseName + ".json")) {
                    pw.println("[");
                    for (int i = 0; i < feedbackList.size(); i++) {
                        pw.print(feedbackList.get(i).toJSON());
                        if (i < feedbackList.size() - 1) pw.println(",");
                    }
                    pw.println("\n]");
                }
            }
            default -> System.out.println("Unsupported format");
        }
        System.out.println("Report exported as " + baseName + "." + format);
    }
}