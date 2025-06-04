package Login;

import java.util.Random;

public class EmailGenerator {

    public static void main(String[] args) {
        String baseEmail = "maverickksoul@gmail.com";
        String generatedEmail = generateRandomEmail(baseEmail);
        System.out.println("Generated Email: " + generatedEmail);
    }

    public static String generateRandomEmail(String baseEmail) {
        // Split local part and domain
        String[] parts = baseEmail.split("@");
        String localPart = parts[0];
        String domain = parts[1];

        // Generate random number
        int randomNum = new Random().nextInt(90000) + 10000; // 5-digit

        // Combine
        return localPart + "+" + randomNum + "@" + domain;
    }
}

