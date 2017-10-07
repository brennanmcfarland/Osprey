package khe.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class will handle the server operations for Osprey.
 *
 */
public class OspServer {
    /* Create and bind sockets to listen for the signal
       When it gets the signal, checks if in SHLEEP mode
       If not in SHLEEP, send an email to a configured address */

    // Server behavior
    private int portNumber;
    private ServerSocket serverSocket;
    private Socket clientSocket;

    // Email behavior
    private int sleepTimer;
    private String[] emailRecip;    // This should be read from config file


    public static void main(String[] args) {

        /* USAGE
           java OspServer <PORT_NUMBER> <SLEEP_TIMER>
         */
        final String usage = "USAGE: java OspServer <PORT_NUMBER> <SLEEP_TIMER>";

        if (args.length != 2) {
            System.err.println(usage);
            System.exit(1);
        }

        ServerSocket serverSocket;
        Socket clientSocket;

        int portNumber = Integer.parseInt(args[0]);
        int sleepTimer = Integer.parseInt(args[1]);


        // Keep server running
        while (true) {
            String[] emailRecip;

            try {
                serverSocket = new ServerSocket(portNumber);
                portNumber = portNumber;
                clientSocket = serverSocket.accept();    // Waits until a connection is made
            }
            catch (IOException q) {
                System.exit(1);
            }



            // After the connection is established and the socket receives a signal
            // Send email to configured recipients to tell them to shut up


            // Avoid spamming emails
            try {
                Thread.sleep(sleepTimer * 1000 * 60);
            }
            catch (InterruptedException e) {
                System.out.println("Thread interrupted!");
            }
        }




    }
}
