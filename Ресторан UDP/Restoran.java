package obid.restoran.udp.vtor;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Handler;

public class Restoran {

    static class Handler extends Thread {


        private final DatagramPacket datagramPacket1;

        public Handler(DatagramPacket datagramPacket1) {
            this.datagramPacket1 = datagramPacket1;
        }

        @Override
        public void run() {
            try {
                String baranje = new String(datagramPacket1.getData(), 0, datagramPacket1.getLength());
                if (baranje.startsWith("NAPRAVI_NARACKA")) {
                    String[] parts = baranje.split("@");
                    String jadenje = parts[2];

                    Thread.sleep(5000);

                    String naracka = "Narackata " + jadenje + " e gotova";
                    byte[] paket = naracka.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(paket, paket.length, datagramPacket1.getAddress(), datagramPacket1.getPort());
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(datagramPacket);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(0);
        int port = datagramSocket.getLocalPort();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Vnesi jadenja za vo menito, oddeleni so zapirka: ");
        String jadenja = scanner.nextLine();

        String meni = "REGISTRIRAJ_MENI@" + port + "@" + jadenja;
        byte[] meniBytes = meni.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(meniBytes, meniBytes.length, InetAddress.getLocalHost(), 11223);
        DatagramSocket socket = new DatagramSocket();
        socket.send(datagramPacket);

        byte[] odgovor = new byte[500];
        DatagramPacket packet = new DatagramPacket(odgovor, odgovor.length);
        socket.receive(packet);

        String rsp = new String(packet.getData(), 0, packet.getLength());
        System.out.println(rsp);

        while (true) {
            byte[] data = new byte[500];
            DatagramPacket datagramPacket1 = new DatagramPacket(data, data.length);
            datagramSocket.receive(datagramPacket1);
            new Handler(datagramPacket1).start();
        }

    }
}
