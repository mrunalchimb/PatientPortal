package Login;

import javax.mail.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GmailOTPReader {
    public static String otp;

    public static String waitForLatestOtpEmail(int maxWaitSeconds) {
        String host = "imap.gmail.com";
        String username = "mrunalchim@gmail.com";
        String appPassword = "pqqq ieqx lpxf gjei";

        try {
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect(host, username, appPassword);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            int initialMessageCount = inbox.getMessageCount();
            String lastSeenMessageId = null;

            if (initialMessageCount > 0) {
                Message lastMsg = inbox.getMessage(initialMessageCount);
                String[] headers = lastMsg.getHeader("Message-ID");
                if (headers != null && headers.length > 0) {
                    lastSeenMessageId = headers[0];
                }
            }

            int waited = 0;
            while (waited < maxWaitSeconds) {
                Thread.sleep(5000);
                waited += 5;

                inbox.close(false);
                inbox.open(Folder.READ_ONLY);

                int newCount = inbox.getMessageCount();
                if (newCount > 0) {
                    Message latestMsg = inbox.getMessage(newCount);
                    String[] headers = latestMsg.getHeader("Message-ID");
                    String currentId = (headers != null && headers.length > 0) ? headers[0] : "";

                    if (!currentId.equals(lastSeenMessageId)) {
                        String content = getTextFromMessage(latestMsg);
                        String otp = extractOtp(content);
                        if (otp != null) {
                            System.out.println("✅ New OTP found!");
                            return otp;
                        } else {
                            System.out.println("❌ New email but no OTP in it.");
                        }
                    } else {
                        System.out.println("⏳ Still waiting for a new email...");
                    }
                }
            }

            System.out.println("❌ Timeout waiting for new OTP email.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String extractOtp(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : null;
    }

    private static String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart part = multipart.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    return part.getContent().toString();
                } else if (part.isMimeType("text/html")) {
                    String html = (String) part.getContent();
                    return htmlToPlainText(html);
                }
            }
        }
        return "";
    }

    private static String htmlToPlainText(String html) {
        return html.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
    }

    public static void main(String[] args) {
        otp = waitForLatestOtpEmail(60); // wait up to 60 seconds for new email
        System.out.println("OTP: " + otp);
    }
}
