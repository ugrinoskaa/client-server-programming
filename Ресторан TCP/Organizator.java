package obid.restoran.tcp.prv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Organizator {
    static Map<String, String> meni = new ConcurrentHashMap<>();

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
                String baranje = in.readLine();
                if (baranje.startsWith("REGISTRIRAJ_MENI")) {
                    String[] parts = baranje.split("@");
                    String idRestoran = parts[1];
                    String menito = parts[2];

                    meni.put(idRestoran, menito);
                    out.println("registrirano!");
                } else if (baranje.startsWith("BARA_MENI")) {
                    String rezultat = "";
                    for (String restoran : meni.keySet()) {
                        rezultat += restoran + ": " + meni.get(restoran) + " | ";
                    }
                    out.println(rezultat);
                } else if (baranje.startsWith("NAPRAVI_NARACKA")) {
                    String[] parts = baranje.split("@");
                    String restoran = parts[1];
                    String menito = parts[2];

                    Socket socket1 = new Socket("localhost", Integer.parseInt(restoran));
                    BufferedReader restoranIn = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                    PrintWriter restoranOut = new PrintWriter(socket1.getOutputStream(), true);
                    restoranOut.println(baranje);

                    String odgovor = restoranIn.readLine();
                    out.println(odgovor);
                }


            } catch (IOException e) {

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
