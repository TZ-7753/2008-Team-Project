package project.util;

import java.util.UUID;

public class UniqueUserIDGenerator {
    public static String generateUniqueUserID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    public static void main(String[] args) {
        String uniqueUserID = generateUniqueUserID();
        System.out.println("Unique User ID: " + uniqueUserID);
    }
}