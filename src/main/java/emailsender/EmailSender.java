package emailsender;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    public static boolean sendEmail(String toEmail, String subject,  String mailText) {
        String fromEmail = "aryankansal113@gmail.com";
        String appPassword = "aapu ywky hejr peqt";
        // String subject = "Real Estate Portal Notification";
        // 1. Configure SMTP server settings
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); // Fixed server host name
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        // 2. Log into the mail server using your App Password
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
        try {
            // 3. Create the email content
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "Real Estate Portal"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            
            // Check if mailText contains HTML tags
            if (mailText != null && (mailText.contains("<html>") || mailText.contains("<div") || mailText.contains("<p>"))) {
                message.setContent(mailText, "text/html; charset=utf-8");
            } else {
                message.setText(mailText);
            }

            // 4. Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        try {
            // True forces strict parsing rules
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    public static void sendCustomerEmailAsync(String action, String toEmail, String name, String mobile, String customerType, String address, String city) {
        if (!isValidEmail(toEmail)) return;
        new Thread(() -> {
            String subject = "Customer " + action + " Confirmation - Real Estate Portal";
            String html = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #ffffff;\">" +
                    "  <div style=\"background-color: #2c3e50; color: #ffffff; padding: 15px; border-radius: 6px 6px 0 0; text-align: center;\">" +
                    "    <h2 style=\"margin: 0;\">Real Estate Portal</h2>" +
                    "    <p style=\"margin: 5px 0 0 0; font-size: 14px;\">Customer " + action + " Details</p>" +
                    "  </div>" +
                    "  <div style=\"padding: 20px;\">" +
                    "    <p>Dear <strong>" + (name != null ? name : "Customer") + "</strong>,</p>" +
                    "    <p>Your customer details have been successfully " + action.toLowerCase() + "d in our system:</p>" +
                    "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Mobile Number:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (mobile != null ? mobile : "-") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Name:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (name != null ? name : "-") + "</td></tr>" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Customer Type:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (customerType != null ? customerType : "-") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Email:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + toEmail + "</td></tr>" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Address:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (address != null ? address : "-") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">City:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (city != null ? city : "-") + "</td></tr>" +
                    "    </table>" +
                    "    <p style=\"margin-top: 20px;\">If you have any questions or need to make further updates, please feel free to reach out to us.</p>" +
                    "    <p>Best regards,<br><strong>Real Estate Management Team</strong></p>" +
                    "  </div>" +
                    "</div>";
            sendEmail(toEmail, subject, html);
        }).start();
    }

    public static void sendPropertyEmailAsync(String action, String toEmail, String sellerName, String mobile, String propName,
                                             String address, String area, String city, String size,
                                             String front, String rear, String left, String right,
                                             String direction, String usageType, String statusType,
                                             String approvedBy, String price, String otherInfo) {
        if (!isValidEmail(toEmail)) return;
        new Thread(() -> {
            String subject = "Property Listing " + action + " - " + (propName != null ? propName : "Real Estate Portal");
            String html = "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px; background-color: #ffffff;\">" +
                    "  <div style=\"background-color: #27ae60; color: #ffffff; padding: 15px; border-radius: 6px 6px 0 0; text-align: center;\">" +
                    "    <h2 style=\"margin: 0;\">Real Estate Portal</h2>" +
                    "    <p style=\"margin: 5px 0 0 0; font-size: 14px;\">Property Listing " + action + " Confirmation</p>" +
                    "  </div>" +
                    "  <div style=\"padding: 20px;\">" +
                    "    <p>Hello <strong>" + (sellerName != null ? sellerName : "Property Owner") + "</strong>,</p>" +
                    "    <p>Your property listing details on our Real Estate Management Portal are provided below:</p>" +
                    "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Property Name:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (propName != null ? propName : "-") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Contact Mobile:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (mobile != null ? mobile : "-") + "</td></tr>" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">City / Area:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (city != null ? city : "") + " / " + (area != null ? area : "") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Address:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (address != null ? address : "-") + "</td></tr>" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Dimensions & Size:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (size != null ? size : "-") + " sq. yds (Front: " + (front != null ? front : "-") + ", Rear: " + (rear != null ? rear : "-") + ", Left: " + (left != null ? left : "-") + ", Right: " + (right != null ? right : "-") + ")</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Facing Direction:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (direction != null ? direction : "-") + "</td></tr>" +
                    "      <tr style=\"background-color: #f9f9f9;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Usage / Land Type:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (usageType != null ? usageType : "-") + " / " + (statusType != null ? statusType : "-") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Approved By:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (approvedBy != null ? approvedBy : "-") + "</td></tr>" +
                    "      <tr style=\"background-color: #e8f8f5;\"><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold; color: #27ae60;\">Demanded Price:</td><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold; color: #27ae60;\">₹" + (price != null ? price : "0") + "</td></tr>" +
                    "      <tr><td style=\"padding: 10px; border: 1px solid #ddd; font-weight: bold;\">Remarks / Info:</td><td style=\"padding: 10px; border: 1px solid #ddd;\">" + (otherInfo != null ? otherInfo : "-") + "</td></tr>" +
                    "    </table>" +
                    "    <p style=\"margin-top: 20px;\">Thank you for listing your property on our platform.</p>" +
                    "    <p>Best regards,<br><strong>Real Estate Management Team</strong></p>" +
                    "  </div>" +
                    "</div>";
            sendEmail(toEmail, subject, html);
        }).start();
    }
}


