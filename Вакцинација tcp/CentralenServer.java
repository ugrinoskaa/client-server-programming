package obid.vakcinacija.tcp.tret;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CentralenServer {
    static List<String> punktovi = new CopyOnWriteArrayList<>();

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
                if (action.startsWith("NAJAVA")) {
                    String[] parts = action.split("@");
                    String id = parts[1];
                    punktovi.add(id);
                    out.println(id);
                } else if (action.startsWith("ODJAVA")) {
                    String[] parts = action.split("@");
                    String id = parts[1];
                    punktovi.remove(id);

                    out.println("Uspesna odjava!");
                } else if (action.startsWith("LISTA_PUNKTOVI")) {
                    String rezultat = "";
                    for (String punkt : punktovi) {
                        rezultat += punkt + ",";
                    }
                    out.println(rezultat);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(11223);

        while (true) {
            Socket connection = serverSocket.accept();
            new Handler(connection).start();
        }
    }
}
