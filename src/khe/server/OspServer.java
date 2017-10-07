package khe.server;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;

/**
 * This class will handle the server operations for Osprey.
 *
 */
public class OspServer {

    public static void main(String[] args) {

        /* USAGE
           java OspServer <PORT_NUMBER> <SLEEP_TIMER>
         */
        final String usage = "USAGE: java OspServer <PORT_NUMBER> <SLEEP_TIMER>";

        if (args.length != 2) {
            System.err.println(usage);
            System.exit(1);
        }

        //////////////////
        /* SERVER SETUP */
        //////////////////

        int portNumber = Integer.parseInt(args[0]);
        int sleepTimer = Integer.parseInt(args[1]);

        // Read a text file for recipients in form of <email>\n<email>\n...
        String[] emailRecip = null;
        try {
            BufferedReader recipFile = new BufferedReader(new FileReader(
                    "src/khe/server/recip.txt"));
            emailRecip = recipFile.lines().toArray(String[]::new);
        }
        catch (FileNotFoundException e) {
            System.out.println("Recipients file not found");
            System.exit(1);
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException q) {
            System.exit(1);
        }



        ////////////////////
        /* RUNNING SERVER */
        ////////////////////

        while (true) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
            }
            catch (IOException r) {
                System.exit(1);
            }

            OspServerChick connection = new OspServerChick(clientSocket, sleepTimer, emailRecip);
            connection.start();
        }
    }
}
