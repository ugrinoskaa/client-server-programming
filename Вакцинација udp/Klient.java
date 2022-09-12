package obid.vakcinacija.udp.tret;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Klient {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("----------");
            System.out.println("1. Lista punktovi");
            System.out.println("2. Zemi reden broj");

            int command = Integer.parseInt(scanner.nextLine());
            if (command == 1) {
                String lista = "LISTA_PUNKTOVI";
                byte[] listaBytes = lista.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(listaBytes, listaBytes.length, InetAddress.getLocalHost(), 11223);
                DatagramSocket socket = new DatagramSocket();
                socket.send(datagramPacket);

                byte[] odgovor = new byte[500];
                DatagramPacket packet = new DatagramPacket(odgovor, odgovor.length);
                socket.receive(packet);

                String rsp = new String(packet.getData(), 0, packet.getLength());
                System.out.println(rsp);


            } else if (command == 2) {
                System.out.print("Vnesi punkt: ");
                String punkt = scanner.nextLine();

                String baranje = "REDEN_BROJ";
                byte[] baranjeBytes = baranje.getBytes();
                DatagramPacket packet1 = new DatagramPacket(baranjeBytes, baranjeBytes.length, InetAddress.getLocalHost(), Integer.parseInt(punkt));
                DatagramSocket socket1 = new DatagramSocket();
                socket1.send(packet1);

                byte[] odgovor = new byte[500];
                DatagramPacket packet = new DatagramPacket(odgovor, odgovor.length);
                socket1.receive(packet);

                String rsp = new String(packet.getData(), 0, packet.getLength());
                System.out.println(rsp);

            }

        }
    }
}
