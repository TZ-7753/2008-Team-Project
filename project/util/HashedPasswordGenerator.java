package project.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashedPasswordGenerator {
    private static final String SALT = "Salty";

    public static String hashPassword(char[] password) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] saltedPasswordBytes = concatenateBytes(SALT.getBytes(), new String(password).getBytes());

            md.update(saltedPasswordBytes);

            byte[] hashedPasswordBytes = md.digest();

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concatenateBytes(byte[] arr1, byte[] arr2) {
        byte[] combined = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
        return combined;
    }

    public static void main(String[] args) {
        char[] password = "test".toCharArray();
        String hashedPassword = hashPassword(password);

        System.out.println("Original Password: " + String.valueOf(password));
        System.out.println("Hashed Password: " + hashedPassword);
    }
}