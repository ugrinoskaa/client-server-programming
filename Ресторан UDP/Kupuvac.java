package obid.restoran.udp.vtor;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Kupuvac {   //KLIENT
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("---------");
            System.out.println("1. Pobaraj meni");
            System.out.println("2. Napravi naracka");

            int command = Integer.parseInt(scanner.nextLine());
            if (command == 1) {
                String meni = "BARA_MENI";
                byte[] meniBytes = meni.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(meniBytes, meniBytes.length, InetAddress.getLocalHost(), 11223);
                DatagramSocket socket = new DatagramSocket();
                socket.send(datagramPacket);

                byte[] odgovor = new byte[500];
                DatagramPacket packet = new DatagramPacket(odgovor, odgovor.length);
                socket.receive(packet);

                String rsp = new String(packet.getData(), 0, packet.getLength());
                System.out.println(rsp);


            } else if (command == 2) {
                System.out.print("Vnesi restoran: ");
                String restoran = scanner.nextLine();
                System.out.print("Vnesi jadenje: ");
                String jadenje = scanner.nextLine();

                String naracka = "NAPRAVI_NARACKA@" + restoran + "@" + jadenje;
                byte[] narackaBytes = naracka.getBytes();
                DatagramPacket packet = new DatagramPacket(narackaBytes, narackaBytes.length, InetAddress.getLocalHost(), 11223);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);

                byte[] odgovor1 = new byte[500];
                DatagramPacket datagramPacket = new DatagramPacket(odgovor1, odgovor1.length);
                socket.receive(datagramPacket);

                String rsp = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(rsp);
            }
        }
    }
}
