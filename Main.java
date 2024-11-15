public class Main {
    public static void main(String[] args) {
        // Get singleton instance
        UserManagementSystem system = UserManagementSystem.getInstance();
        
        // Test authentication
        User admin = new User("A001", "admin", "admin@test.com", "admin123", UserType.ADMIN);
        User powerUser = new User("P001", "power", "power@test.com", "power123", UserType.POWER);
        User regularUser = new User("R001", "regular", "regular@test.com", "regular123", UserType.REGULAR);

        // Get appropriate interfaces
        IAdminOperations adminInterface = (IAdminOperations) system.getUserInterface(admin);
        IUserWriter powerInterface = (IUserWriter) system.getUserInterface(powerUser);
        IUserReader regularInterface = (IUserReader) system.getUserInterface(regularUser);

        // Test operations
        System.out.println("Testing user operations:");
        
        // Admin operations
        System.out.println("Admin adding user: " + 
            adminInterface.addUser(new User("R002", "test", "test@test.com", "test123", UserType.REGULAR)));
        System.out.println("Admin updating privileges: " + 
            adminInterface.updateUserPrivileges("R002", UserType.POWER));

        // Power user operations
        System.out.println("Power user adding user: " + 
            powerInterface.addUser(new User("R003", "test2", "test2@test.com", "test123", UserType.REGULAR)));

        // Regular user operations
        System.out.println("\nViewing users as regular user:");
        regularInterface.viewUsers().forEach(u -> 
            System.out.println(u.getUsername() + " - " + u.getUserType()));
    }
}