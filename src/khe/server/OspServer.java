package khe.server;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.ArrayList;

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
            emailRecip = recipsFromFile();
        }
        catch (FileNotFoundException e) {
            System.err.println("Recipients file not found");
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
                System.out.println("Watching, waiting, commiserating");
                clientSocket = serverSocket.accept();
            }
            catch (IOException r) {
                System.exit(1);
            }

            for (String recip : emailRecip)
                System.out.println(recip);

            OspServerChick connection = new OspServerChick(clientSocket, sleepTimer, emailRecip);
            connection.start();
        }
    }

    private static String[] recipsFromFile() throws FileNotFoundException {
        BufferedReader file = new BufferedReader(new FileReader(
                "/home/elopez/Documents/OspreyDependencies/recip.txt"));

        ArrayList<String> recipLines = new ArrayList<>();
        String[] q;

        try {
            String address;
            while ((address = file.readLine()) != null) {
                recipLines.add(address);
            }
        }
        catch (IOException e ) {
            System.out.println(e.getMessage());
        }

        q = new String[recipLines.size()];

        return recipLines.toArray(q);
    }
}
