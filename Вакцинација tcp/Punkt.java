package obid.vakcinacija.tcp.tret;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Punkt {
    static Integer REDENBROJ = 0;

    static class Handler extends Thread {


        private final Socket connection;

        public Handler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                out.println(++REDENBROJ);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Server extends Thread {


        private final ServerSocket serverSocket;

        public Server(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket connection = serverSocket.accept();
                    new Handler(connection).start();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(0);
        int port = serverSocket.getLocalPort();

        Socket socket = new Socket("localhost", 11223);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println("NAJAVA@" + port);

        String id = in.readLine();

        Thread thread = new Server(serverSocket);
        thread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Vnesi 2 za odjava:");
        int command = Integer.parseInt(scanner.nextLine());
        if (command == 2) {
            Socket connection = new Socket("localhost", 11223);
            BufferedReader odjavaIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter odjavaOut = new PrintWriter(connection.getOutputStream(), true);
            odjavaOut.println("ODJAVA@" + id);

            String rsp = odjavaIn.readLine();
            System.out.println(rsp);
        }

    }
}
