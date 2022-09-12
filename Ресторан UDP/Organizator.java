package obid.restoran.udp.vtor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Organizator {
    static Map<String, String> meni = new ConcurrentHashMap<>();

    static class Handler extends Thread {


        private final DatagramPacket datagramPacket;

        public Handler(DatagramPacket datagramPacket) {
            this.datagramPacket = datagramPacket;
        }

        @Override
        public void run() {
            try {
                String baranje = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                if (baranje.startsWith("REGISTRIRAJ_MENI")) {
                    String[] parts = baranje.split("@");
                    String restoran = parts[1];
                    String menito = parts[2];

                    meni.put(restoran, menito);

                    String odgovor = "Registrirano!";
                    byte[] response = odgovor.getBytes();
                    DatagramPacket packet = new DatagramPacket(response, response.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);

                } else if (baranje.startsWith("BARA_MENI")) {
                    String rezultat = "";
                    for (String restoran : meni.keySet()) {
                        rezultat += restoran + ": " + meni.get(restoran) + " | ";
                    }
                    byte[] response = rezultat.getBytes();
                    DatagramPacket packet = new DatagramPacket(response, response.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);

                } else if (baranje.startsWith("NAPRAVI_NARACKA")) {
                    String[] parts = baranje.split("@");
                    String port = parts[1];

                    //prakjam na restoran  SEGA SUM KLIENT
                    byte[] baranjeBytes = baranje.getBytes();
                    DatagramPacket packet1=new DatagramPacket(baranjeBytes, baranjeBytes.length, InetAddress.getLocalHost(), Integer.parseInt(port));
                    DatagramSocket socket1=new DatagramSocket();
                    socket1.send(packet1);

                    //primam od restoran
                    byte[] odgovor=new byte[500];
                    DatagramPacket packet2=new DatagramPacket(odgovor,odgovor.length);
                    socket1.receive(packet2);

                    //mu vrakjam odgovor na kupuvacot nazad   SEGA SUM SERVER
                    String rsp= new String(packet2.getData(), 0 , packet2.getLength());
                    byte[] rspBytes = rsp.getBytes();
                    DatagramPacket packet3=new DatagramPacket(rspBytes, rspBytes.length, datagramPacket.getAddress(),datagramPacket.getPort());
                    DatagramSocket socket2=new DatagramSocket();
                    socket2.send(packet3);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket(11223);

        while (true) {
            byte[] packet = new byte[500];
            DatagramPacket datagramPacket = new DatagramPacket(packet, packet.length);
            datagramSocket.receive(datagramPacket);
            new Handler(datagramPacket).start();
        }
    }
}
