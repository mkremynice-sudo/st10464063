package com.mycompany.login_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Login_System {

    // ================= VARIABLES =================
    private static final Scanner scanner = new Scanner(System.in);
    private static final ArrayList<Message> messages = new ArrayList<>();

    private static String storedUsername = "";
    private static String storedPassword = "";
    private static String storedPhone = "";

    private static int totalMessagesSent = 0;

    // ================= MAIN =================
    public static void main(String[] args) {

        printBanner("QUICKCHAT APPLICATION");

        registerUser();

        if (!loginUser()) {
            System.out.println("\n❌ Too many failed login attempts.");
            return;
        }

        System.out.print("\n📨 How many messages would you like to send? ");

        int maxMessages;

        try {
            maxMessages = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            maxMessages = 5;
        }

        runMainMenu(maxMessages);

        scanner.close();
    }

    // ================= REGISTER =================
    private static void registerUser() {

        printBanner("REGISTRATION");

        while (true) {

            System.out.print("Enter username (_ and max 5 chars): ");

            String username = scanner.nextLine();

            if (checkUserName(username)) {

                storedUsername = username;

                System.out.println("✓ Username accepted");

                break;
            }

            System.out.println("❌ Invalid username");
        }

        while (true) {

            System.out.print("Enter password (8+, capital, number, special): ");

            String password = scanner.nextLine();

            if (checkPassword(password)) {

                storedPassword = password;

                System.out.println("✓ Password accepted");

                break;
            }

            System.out.println("❌ Invalid password");
        }

        while (true) {

            System.out.print("Enter SA phone (+27xxxxxxxxx): ");

            String phone = scanner.nextLine();

            if (checkPhone(phone)) {

                storedPhone = phone;

                System.out.println("✓ Phone number accepted");

                break;
            }

            System.out.println("❌ Invalid phone number");
        }

        System.out.println("\n✅ Registration successful!");
    }

    // ================= LOGIN =================
    private static boolean loginUser() {

        printBanner("LOGIN");

        int attempts = 0;

        while (attempts < 3) {

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            if (username.equals(storedUsername)
                    && password.equals(storedPassword)) {

                System.out.println("\n✅ Login successful!");
                return true;
            }

            attempts++;

            System.out.println("❌ Incorrect details");
        }

        return false;
    }

    // ================= MENU =================
    private static void runMainMenu(int maxMessages) {

        boolean running = true;

        while (running) {

            System.out.println("""
                    
                    ========= QUICKCHAT MENU =========
                    1. Send Message
                    2. Show All Messages
                    3. Show Longest Message
                    4. Search Message by ID
                    5. Search by Recipient
                    6. Delete Message by Hash
                    7. Full Report
                    8. Quit
                    ==================================
                    """);

            System.out.print("Select option: ");

            String option = scanner.nextLine();

            switch (option) {

                case "1":

                    if (messages.size() < maxMessages) {
                        sendMessage();
                    } else {
                        System.out.println("❌ Message limit reached");
                    }

                    break;

                case "2":
                    showMessages();
                    break;

                case "3":
                    displayLongestMessage();
                    break;

                case "4":
                    searchMessageID();
                    break;

                case "5":
                    searchRecipient();
                    break;

                case "6":
                    deleteMessageHash();
                    break;

                case "7":
                    fullReport();
                    break;

                case "8":

                    saveMessagesToFile();

                    System.out.println("\n👋 Goodbye!");
                    System.out.println("Total Sent Messages: "
                            + totalMessagesSent);

                    running = false;

                    break;

                default:
                    System.out.println("❌ Invalid option");
            }
        }
    }

    // ================= SEND MESSAGE =================
    private static void sendMessage() {

        printBanner("NEW MESSAGE");

        System.out.print("Recipient (+27xxxxxxxxx): ");
        String recipient = scanner.nextLine();

        if (!checkPhone(recipient)) {

            System.out.println("❌ Invalid recipient");
            return;
        }

        System.out.print("Enter message (max 250 chars): ");

        String text = scanner.nextLine();

        if (text.length() > 250) {

            System.out.println("❌ Message exceeds 250 characters");
            return;
        }

        Message msg = new Message(
                messages.size() + 1,
                recipient,
                text
        );

        System.out.println("""
                
                1. Send Message
                2. Store Message
                3. Disregard Message
                """);

        System.out.print("Choose option: ");

        String choice = scanner.nextLine();

        switch (choice) {

            case "1":

                msg.markAsSent();

                messages.add(msg);

                totalMessagesSent++;

                System.out.println("\n✓ Message sent!");

                System.out.println(msg.printMessage());

                break;

            case "2":

                msg.storeMessage();

                messages.add(msg);

                System.out.println("\n✓ Message stored");

                break;

            case "3":

                System.out.println("\n✗ Message disregarded");

                break;

            default:

                System.out.println("❌ Invalid option");
        }
    }

    // ================= SHOW MESSAGES =================
    private static void showMessages() {

        if (messages.isEmpty()) {

            System.out.println("📭 No messages available");
            return;
        }

        for (Message msg : messages) {

            System.out.println(msg.printMessage());
        }
    }

    // ================= LONGEST MESSAGE =================
    private static void displayLongestMessage() {

        if (messages.isEmpty()) {
            return;
        }

        Message longest = messages.get(0);

        for (Message msg : messages) {

            if (msg.getMessage().length()
                    > longest.getMessage().length()) {

                longest = msg;
            }
        }

        System.out.println("\n📌 Longest Message:");
        System.out.println(longest.getMessage());
    }

    // ================= SEARCH ID =================
    private static void searchMessageID() {

        System.out.print("Enter Message ID: ");

        String id = scanner.nextLine();

        for (Message msg : messages) {

            if (msg.getMessageID().equals(id)) {

                System.out.println(msg.printMessage());
                return;
            }
        }

        System.out.println("❌ Message not found");
    }

    // ================= SEARCH RECIPIENT =================
    private static void searchRecipient() {

        System.out.print("Enter recipient number: ");

        String recipient = scanner.nextLine();

        boolean found = false;

        for (Message msg : messages) {

            if (msg.getRecipient().equals(recipient)) {

                System.out.println(msg.printMessage());

                found = true;
            }
        }

        if (!found) {

            System.out.println("❌ No messages found");
        }
    }

    // ================= DELETE HASH =================
    private static void deleteMessageHash() {

        System.out.print("Enter message hash: ");

        String hash = scanner.nextLine();

        Iterator<Message> iterator = messages.iterator();

        while (iterator.hasNext()) {

            Message msg = iterator.next();

            if (msg.getMessageHash().equals(hash)) {

                iterator.remove();

                System.out.println("✓ Message deleted");

                return;
            }
        }

        System.out.println("❌ Hash not found");
    }

    // ================= REPORT =================
    private static void fullReport() {

        printBanner("FULL MESSAGE REPORT");

        for (Message msg : messages) {

            System.out.println(msg.printMessage());
        }
    }

    // ================= SAVE =================
    private static void saveMessagesToFile() {

        try {

            File file = new File("messages.txt");

            FileWriter writer = new FileWriter(file);

            for (Message msg : messages) {

                writer.write(msg.printMessage());
                writer.write("\n\n");
            }

            writer.close();

        } catch (IOException e) {

            System.out.println("❌ Error saving messages");
        }
    }

    // ================= VALIDATION =================
    static boolean checkUserName(String username) {

        return username.contains("_")
                && username.length() <= 5;
    }

    static boolean checkPassword(String password) {

        String regex =
                "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";

        return Pattern.matches(regex, password);
    }

    static boolean checkPhone(String phone) {

        return Pattern.matches("^\\+27\\d{9}$", phone);
    }

    // ================= UI =================
    private static void printBanner(String title) {

        System.out.println("\n====================================");
        System.out.println(title);
        System.out.println("====================================");
    }

    // ================= INNER CLASS =================
    static class Message {

        private final String messageID;
        private final int messageNumber;
        private final String recipient;
        private final String message;
        private final String messageHash;

        private boolean sent;

        public Message(int number,
                       String recipient,
                       String message) {

            this.messageNumber = number;
            this.recipient = recipient;
            this.message = message;

            this.messageID = generateID();

            this.messageHash = createHash();

            this.sent = false;
        }

        private String generateID() {

            Random random = new Random();

            long id =
                    1000000000L
                            + (long)(random.nextDouble()
                            * 9000000000L);

            return String.valueOf(id);
        }

        private String createHash() {

            String[] words =
                    message.trim().split("\\s+");

            String first =
                    words[0].toUpperCase();

            String last =
                    words[words.length - 1].toUpperCase();

            return messageID.substring(0, 2)
                    + ":"
                    + messageNumber
                    + ":"
                    + first
                    + last;
        }

        public void markAsSent() {
            sent = true;
        }

        public void storeMessage() {

            try {

                File dir = new File("stored_messages");

                if (!dir.exists()) {
                    dir.mkdir();
                }

                FileWriter writer =
                        new FileWriter(
                                "stored_messages/"
                                        + messageID
                                        + ".txt"
                        );

                writer.write(printMessage());

                writer.close();

            } catch (IOException e) {

                System.out.println("❌ Error storing message");
            }
        }

        public String printMessage() {

            return """
                    
                    MESSAGE DETAILS
                    --------------------------
                    ID: %s
                    Hash: %s
                    Recipient: %s
                    Message: %s
                    Status: %s
                    --------------------------
                    """.formatted(
                    messageID,
                    messageHash,
                    recipient,
                    message,
                    sent ? "Sent" : "Stored"
            );
        }

        public String getMessageID() {
            return messageID;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getMessage() {
            return message;
        }

        public String getMessageHash() {
            return messageHash;
        }

        boolean isSent() {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }
    }
}