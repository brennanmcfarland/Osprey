package khe.server;


import protocol.CommandStrings;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import static java.lang.System.out;

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
            BufferedReader recipFile = new BufferedReader(new FileReader("recip.txt"));
            emailRecip = recipFile.lines().toArray(String[]::new);
        }
        catch (FileNotFoundException e) {
            out.println("Recipients file not found");
            System.exit(1);
        }

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException q) {
            System.exit(1);
        }

        try {
            printNetInterfaces();
        }
        catch (SocketException e) {
            System.exit(1);
        }



        ////////////////////
        /* RUNNING SERVER */
        ////////////////////

        while (true) {
            Socket clientSocket = null;

            try {
                out.println("Listening on " + args[0]);
                clientSocket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String res = reader.readLine();
                System.out.println(res);

                if (res.equals(CommandStrings.loudnessNotification)) {
                    out.println("same string");
                    OspServerChick connection = new OspServerChick(clientSocket, sleepTimer, emailRecip);
                    connection.start();
                }

            }
            catch (IOException r) {
                System.exit(1);
            }
        }
    }

    private static void printNetInterfaces() throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            displayInterfaceInformation(netint);


    }

    private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.printf("\n");
    }
}
