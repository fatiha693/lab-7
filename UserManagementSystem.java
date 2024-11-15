import interfaces.*;
import models.*;
import java.io.*;
import java.util.*;

public class UserManagementSystem {
    private static UserManagementSystem instance;
    private List<User> users;
    private List<User> admins;
    private static final String USER_CSV = "User.csv";
    private static final String ADMIN_CSV = "Admin.csv";

    // Private constructor for Singleton
    private UserManagementSystem() {
        users = new ArrayList<>();
        admins = new ArrayList<>();
        loadUsers();
        loadAdmins();
    }

    // Singleton getInstance method
    public static UserManagementSystem getInstance() {
        if (instance == null) {
            instance = new UserManagementSystem();
        }
        return instance;
    }

    // File Operations
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_CSV))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.add(new User(parts[0], parts[1], parts[2], parts[3], UserType.valueOf(parts[4])));
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private void loadAdmins() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_CSV))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                admins.add(new User(parts[0], parts[1], parts[2], parts[3], UserType.ADMIN));
            }
        } catch (IOException e) {
            System.err.println("Error loading admins: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_CSV))) {
            writer.println("UserID,Username,Email,Password,UserType");
            for (User user : users) {
                writer.printf("%s,%s,%s,%s,%s\n",
                    user.getUserId(), user.getUsername(), user.getEmail(),
                    user.getPassword(), user.getUserType());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    // Factory method to create appropriate user interface
    public Object getUserInterface(User user) {
        switch (user.getUserType()) {
            case ADMIN:
                return new AdminUser(user);
            case POWER:
                return new PowerUser(user);
            case REGULAR:
                return new RegularUser(user);
            default:
                return null;
        }
    }

    // Inner classes implementing different user interfaces
    private class RegularUser implements IUserReader {
        private User user;

        public RegularUser(User user) {
            this.user = user;
        }

        @Override
        public List<User> viewUsers() {
            return new ArrayList<>(users);
        }
    }

    private class PowerUser implements IUserWriter {
        private User user;

        public PowerUser(User user) {
            this.user = user;
        }

        @Override
        public List<User> viewUsers() {
            return new ArrayList<>(users);
        }

        @Override
        public boolean addUser(User newUser) {
            if (isEmailUnique(newUser.getEmail()) && isUsernameUnique(newUser.getUsername())) {
                users.add(newUser);
                saveUsers();
                return true;
            }
            return false;
        }
    }

    private class AdminUser implements IAdminOperations {
        private User user;

        public AdminUser(User user) {
            this.user = user;
        }

        @Override
        public List<User> viewUsers() {
            return new ArrayList<>(users);
        }

        @Override
        public boolean addUser(User newUser) {
            if (isEmailUnique(newUser.getEmail()) && isUsernameUnique(newUser.getUsername())) {
                users.add(newUser);
                saveUsers();
                return true;
            }
            return false;
        }

        @Override
        public boolean updateUserPrivileges(String userId, UserType newType) {
            for (User user : users) {
                if (user.getUserId().equals(userId)) {
                    user.setUserType(newType);
                    saveUsers();
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean modifySystemSettings(String operation, String value) {
            // Implementation for system settings modification
            return true;
        }
    }

    // Utility methods
    private boolean isEmailUnique(String email) {
        return users.stream().noneMatch(u -> u.getEmail().equals(email)) &&
               admins.stream().noneMatch(a -> a.getEmail().equals(email));
    }

    private boolean isUsernameUnique(String username) {
        return users.stream().noneMatch(u -> u.getUsername().equals(username)) &&
               admins.stream().noneMatch(a -> a.getUsername().equals(username));
    }

    // Authentication
    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        for (User admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }
}