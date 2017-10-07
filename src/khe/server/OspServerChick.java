package khe.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class OspServerChick extends Thread {
    private Socket socket;
    private int sleepTimer;
    private String[] recipients;

    OspServerChick(Socket socket, int sleepTimer, String[] recipients) {
        this.socket = socket;
        this.sleepTimer = sleepTimer;
        this.recipients = recipients;
    }

    public void run() {
        // Needs to send an email to

        String from = "ei.lopez197@gmail.com";
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("juandolar6@gmail.com", "osprey123");
                    }
                });


        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress((from)));
            message.addRecipients(Message.RecipientType.TO, recipientAddresses(recipients));
            message.setSubject("Noise levels beyond acceptable.");
            message.setText("Cease the noise immediately.");
            Transport.send(message);
            System.out.println("message sent");
        }
        catch (MessagingException q) {
            System.out.println(q.getMessage());
            System.out.println("Message failed to send");
        }


        // Avoid spamming emails
        try {
            Thread.sleep(sleepTimer * 1000 * 60);
        }
        catch (InterruptedException e) {
            System.out.println("Thread interrupted!");
        }
    }

    private InternetAddress[] recipientAddresses(String[] recipients) {
        ArrayList<InternetAddress> addresses = new ArrayList<>();
        InternetAddress[] result;

        for (String recipient : recipients) {
            try {
                addresses.add(new InternetAddress(recipient));
            }
            catch (AddressException e) {
                ; // ignore the failed address
            }
        }

        result = new InternetAddress[addresses.size()];

        return addresses.toArray(result);
    }
}
