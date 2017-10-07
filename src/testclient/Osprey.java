package testclient;

import java.io.IOException;
import java.net.Socket;

public class Osprey {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("USAGE: java Osprey <HOST_NAME> <PORT_NUMBER>");
            System.exit(1);
        }

        Socket sock = null;
        try{
            sock = new Socket(args[0], Integer.parseInt(args[1]));
            /*
            If the connection works, then server should create a thread for the connection
            When that happens, the server should send the email
             */
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


}
