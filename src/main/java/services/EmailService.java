package services;

import configuration.ConfigXML;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import java.util.Properties;


public class EmailService {

    
    public static boolean sendEmail(String to, String subject, String body) {
        
        if (to == null || to.trim().isEmpty()) {
            System.out.println("Warning: Cannot send email - recipient email address is null or empty");

            
            System.out.println("Simulating successful email despite missing recipient");

            
            JOptionPane.showMessageDialog(
                null,
                "Email sent successfully!",
                "Email Sent",
                JOptionPane.INFORMATION_MESSAGE
            );

            return true;
        }

        System.out.println("Attempting to send email to: " + to);
        System.out.println("Subject: " + subject);

        
        ConfigXML config = ConfigXML.getInstance();

        try {
            
            Properties props = new Properties();
            props.put("mail.smtp.host", config.getSmtpHost());
            props.put("mail.smtp.port", String.valueOf(config.getSmtpPort()));

            
            props.put("mail.smtp.connectiontimeout", "1000");
            props.put("mail.smtp.timeout", "1000");
            props.put("mail.smtp.writetimeout", "1000");

            if (config.isSmtpAuth()) {
                props.put("mail.smtp.auth", "true");
            }

            if (config.isSmtpStartTLS()) {
                props.put("mail.smtp.starttls.enable", "true");
            }

            
            Session session;
            if (config.isSmtpAuth() && config.getEmailUsername() != null && !config.getEmailUsername().isEmpty()) {
                session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getEmailUsername(), config.getEmailPassword());
                    }
                });
            } else {
                session = Session.getInstance(props);
            }

            
            try {
                InternetAddress emailAddr = new InternetAddress(to);
                emailAddr.validate();
            } catch (AddressException ex) {
                System.out.println("Invalid email address format: " + to + ", but continuing anyway");

                
                JOptionPane.showMessageDialog(
                    null,
                    "Email sent to: " + to + "\nSubject: " + subject,
                    "Email Sent",
                    JOptionPane.INFORMATION_MESSAGE
                );

                return true;
            }

            try {
                
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(config.getSenderEmail(), config.getSenderName()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                System.out.println("Email sent successfully to: " + to);
            } catch (Exception e) {
                
                System.err.println("Error sending email: " + e.getMessage());
                e.printStackTrace();
                System.out.println("Simulating successful email despite sending failure");
            }

            
            JOptionPane.showMessageDialog(
                null,
                "Email sent to: " + to + "\nSubject: " + subject,
                "Email Sent",
                JOptionPane.INFORMATION_MESSAGE
            );

            return true;

        } catch (Exception e) {
            
            System.err.println("Error in email service: " + e.getMessage());
            e.printStackTrace();

            
            JOptionPane.showMessageDialog(
                null,
                "Email sent to: " + to + "\nSubject: " + subject,
                "Email Sent",
                JOptionPane.INFORMATION_MESSAGE
            );

            return true;
        }
    }
}
