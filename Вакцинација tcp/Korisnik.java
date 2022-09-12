package obid.vakcinacija.tcp.tret;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Korisnik {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("------------");
            System.out.println("1. Lista na punktovi");
            System.out.println("2. Zemi reden broj od punkt");

            int command = Integer.parseInt(scanner.nextLine());
            if (command == 1) {
                Socket connection = new Socket("localhost", 11223);
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                PrintWriter out = new PrintWriter(connection.getOutputStream(), true);

                out.println("LISTA_PUNKTOVI");

                String odgovor = in.readLine();
                System.out.println(odgovor);

            } else if (command == 2) {
                System.out.print("Vnesi punkt: ");
                String punkt = scanner.nextLine();

                Socket socket = new Socket("localhost", Integer.parseInt(punkt));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("REDEN_BROJ");

                String brojPunkt = in.readLine();
                System.out.println(brojPunkt);
            }
        }
    }
}
