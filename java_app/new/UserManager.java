import java.util.*;
import java.io.*;

public class UserManager {
    private final Map<String, String> users = new HashMap<>();
    private static final String USER_FILE = "users_db.csv";

    public UserManager() {
        loadUsers();
    }

    public void seedUsers() {
        if (!users.containsKey("admin")) {
            users.put("admin", "admin123");
            saveUsers();
        }
    }

    public String login(Scanner scanner) {
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if (users.containsKey(user) && users.get(user).equals(pass)) {
            System.out.println("Hello " + user + "!");
            return user;
        } else {
            System.out.println("Invalid credentials");
            return null;
        }
    }

    public void register(Scanner scanner) {
        System.out.print("Choose username: ");
        String user = scanner.nextLine();
        System.out.print("Choose password: ");
        String pass = scanner.nextLine();
        users.put(user, pass);
        saveUsers();
        System.out.println("Registration successful!");
    }

    private void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load users");
        }
    }

    private void saveUsers() {
        try (PrintWriter pw = new PrintWriter(USER_FILE)) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                pw.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Failed to save users");
        }
    }
}