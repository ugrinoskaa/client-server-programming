package obid.restoran.tcp.prv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Kupuvac {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("---------");
            System.out.println("1. Pobaraj meni");
            System.out.println("2. Napravi naracka");

            int command = Integer.parseInt(scanner.nextLine());
            if (command == 1) {
                Socket socket = new Socket("localhost", 11223);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("BARA_MENI");
                String meni = in.readLine();
                System.out.println(meni);

            } else if (command == 2) {
                System.out.print("Vnesi restoran: ");
                String restoran = scanner.nextLine();
                System.out.print("Vnesi jadenje: ");
                String jadenje = scanner.nextLine();
                Socket socket = new Socket("localhost", 11223);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("NAPRAVI_NARACKA@" + restoran + "@" + jadenje);

                String odgovor = in.readLine();
                System.out.println(odgovor);
            }
        }
    }
}
