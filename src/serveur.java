import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class serveur extends Thread {
    // ArrayList to store PrintWriter instances for all connected clients
    static ArrayList<PrintWriter> clientsWriters = new ArrayList<>();

    // Variable to store the secret value
    static int Secret_Value;

    // Main method to start the server
    public static void main(String[] args) {
        new serveur().start();
    }

    // Run method for the server thread
    public void run() {
        try {
            // Creates a ServerSocket that will listen for incoming connections on port 9999.
            ServerSocket s_server = new ServerSocket(9999);

            // The server chooses a random value and multiplies it by 100.
            Secret_Value = new Random().nextInt(100);
            Secret_Value *= 100;

            // Prints the secret value to the console.
            System.out.println("Valeur secrete: " + Secret_Value);

            while (true) {
                // Accepts an incoming connection and creates a new Socket to handle that connection.
                Socket s = s_server.accept();

                // Gets the input/output streams of the socket for communication with the client.
                OutputStream out = s.getOutputStream();
                PrintWriter pw = new PrintWriter(out, true);

                // Adds the PrintWriter of the client to the list.
                clientsWriters.add(pw);

                // Creates a new instance of the Repartiteur class to handle this client and starts a new thread.
                new Repartiteur(s, pw).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to close all connections
    public static void cloturerToutesLesConnexions() {
        for (PrintWriter clientWriter : clientsWriters) {
            // Closes the PrintWriter of each client.
            clientWriter.close();
        }
        System.exit(0);  // This can be used to terminate the application if necessary.
    }
}
