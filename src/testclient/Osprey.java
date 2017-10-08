package testclient;

import protocol.CommandStrings;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class Osprey {

    //ensure a host and port are provided as arguments
    private static void validateInput(String[] args) {
        if (args.length != 2) {
            System.err.println("USAGE: java Osprey <HOST_NAME> <PORT_NUMBER>");
            System.exit(1);
        }
    }

    public static void main(String[] args) {

        validateInput(args);

        try{
            //initialize the socket connection and listener
            Listener audioAmplitudeListener = new Listener(new AmplitudeThreshold(.001f));
            //every time the audio amplitude limit is exceeded, notify the server
            while(audioAmplitudeListener.listen() != null) {
                Socket connectionSocket = new Socket(args[0], Integer.parseInt(args[1]));
                //send a message to the server
                BufferedWriter connectionWriter =
                        new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
                connectionWriter.write(CommandStrings.loudnessNotification);
                connectionWriter.flush();
                connectionWriter.close();
                System.out.println("Loudness notification sent to server");
                Thread.sleep(CommandStrings.SLEEPTIME);
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (InterruptedException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


}
