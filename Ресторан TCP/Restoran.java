package obid.restoran.tcp.prv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Restoran {

    static class Handler extends Thread {


        private final Socket connection;

        public Handler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String action = in.readLine();
                if (action.startsWith("NAPRAVI_NARACKA")) {
                    String[] parts = action.split("@");
                    String jadenje = parts[2];

                    Thread.sleep(5000);

                    out.println("Narackata " + jadenje + " e gotova.");
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();

        Socket socket = new Socket("localhost", 11223);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Vnesi jadenja za vo meni oddeleni so zapirka: ");
        String jadenja = scanner.nextLine();
        out.println("REGISTRIRAJ_MENI@" + port + "@" + jadenja);
        String odgovor = in.readLine();
        System.out.println(odgovor);

        while (true) {  //za da povekje kupuvaci mozat da naracat preku organizator do ist restoran
            Socket connection = serverSocket.accept();
            new Handler(connection).start();
        }
    }
}
