package obid.vakcinacija.udp.tret;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CentralenServer {
    static List<String> punktovi = new CopyOnWriteArrayList<>();

    static class Handler extends Thread {


        private final DatagramPacket datagramPacket;

        public Handler(DatagramPacket datagramPacket) {
            this.datagramPacket = datagramPacket;
        }

        @Override
        public void run() {
            try {
                String baranje = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                if (baranje.startsWith("NAJAVA")) {
                    String[] parts = baranje.split("@");
                    String id = parts[1];
                    punktovi.add(id);

                    byte[] idBytes = id.getBytes();
                    DatagramPacket packet = new DatagramPacket(idBytes, idBytes.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);

                } else if (baranje.startsWith("ODJAVA")) {
                    String[] parts = baranje.split("@");
                    String id = parts[1];

                    punktovi.remove(id);

                    String odgovor = "Uspesna odjava!";
                    byte[] odgovorBytes = odgovor.getBytes();
                    DatagramPacket packet1 = new DatagramPacket(odgovorBytes, odgovorBytes.length, datagramPacket.getAddress(), datagramPacket.getPort());
                    DatagramSocket socket1 = new DatagramSocket();
                    socket1.send(packet1);


                } else if (baranje.startsWith("LISTA_PUNKTOVI")) {
                    String rezultat = "";
                    for (String punkt : punktovi) {
                        rezultat += punkt + ",";
                    }
                    byte[] rspBytes = rezultat.getBytes();
                    DatagramPacket packet2=new DatagramPacket(rspBytes,rspBytes.length, datagramPacket.getAddress(),datagramPacket.getPort());
                    DatagramSocket socket2=new DatagramSocket();
                    socket2.send(packet2);

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
