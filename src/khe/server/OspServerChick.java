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
        String host = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);


        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress((from)));
            message.setRecipients(Message.RecipientType.TO, recipientAddresses(recipients));
            message.setSubject("Noise levels beyond acceptable.");
            message.setText("Cease the noise immediately.");
            Transport.send(message);
        }
        catch (Exception q) {
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
        InternetAddress[] result = new InternetAddress[8];

        for (String recipient : recipients) {
            try {
                addresses.add(new InternetAddress(recipient));
            }
            catch (AddressException e) {
                ; // ignore the failed address
            }
        }

        return addresses.toArray(result);
    }
}
