import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.InputStream;

public class Repartiteur extends Thread {
    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;

    // Constructor for Repartiteur class
    public Repartiteur(Socket socket, PrintWriter pw) {
        super();
        this.socket = socket;
        this.pw = pw;

        try {
            // Obtains input/output streams for communication with the client.
            InputStream input = socket.getInputStream();
            InputStreamReader ins = new InputStreamReader(input);
            br = new BufferedReader(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run method for the thread
    public void run() {
        try {
            // Informs the client about the successful connection.
            informerClient("Welcome ! Propose a number  ", pw);

            while (true) {
                // Reads the client's proposition.
                String propositionStr = br.readLine();
                int proposition = Integer.parseInt(propositionStr);

                // Compares the client's proposition with the secret value.
                if (proposition < serveur.Secret_Value) {
                    // The proposition is smaller.
                    informerClient("The give value is less then the secret value . TRY AGAIN !", pw);
                } else if (proposition > serveur.Secret_Value) {
                    // The proposition is larger.
                    informerClient("The given value is bigger then the secret value . TRY AGAIN !", pw);
                } else {
                    // The client has guessed the secret value.
                    String ipWinner = socket.getRemoteSocketAddress().toString();
                    informerClient("Congratulation ! you are the winner the number was indeed " +proposition ,pw);
                    informerTousLesClients("The winner is  l'@IP : " + ipWinner +" He found the secret value " +proposition);
                    System.out.println("");
                    informerTousLesClients(" GAME OVER ! ");
                    serveur.cloturerToutesLesConnexions();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to inform all clients.
    private void informerTousLesClients(String message) {
        for (PrintWriter clientWriter : serveur.clientsWriters) {
            clientWriter.println(message);
        }
    }

    // Method to inform a specific client.
    private void informerClient(String message, PrintWriter pw) {
        pw.println(message);
    }
}
