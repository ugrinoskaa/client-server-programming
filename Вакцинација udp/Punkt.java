package obid.vakcinacija.udp.tret;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Punkt {
    static Integer REDENBROJ=0;
    static class Server extends Thread{
        static class Handler extends Thread{


            private final DatagramPacket datagramPacket;

            public Handler(DatagramPacket datagramPacket) {
                this.datagramPacket=datagramPacket;
            }

            @Override
            public void run() {
                try {
                    String baranje = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    if (baranje.startsWith("REDEN_BROJ")) {
                        String broj = String.valueOf(++REDENBROJ);

                        byte[] brojBytes = broj.getBytes();
                        DatagramPacket packet = new DatagramPacket(brojBytes, brojBytes.length, datagramPacket.getAddress(), datagramPacket.getPort());
                        DatagramSocket socket = new DatagramSocket();
                        socket.send(packet);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }


        private final DatagramSocket datagramSocket;

        public Server(DatagramSocket datagramSocket) {
            this.datagramSocket=datagramSocket;
        }

        @Override
        public void run() {
            try{
                while(true){
                    byte[] packet=new byte[500];
                    DatagramPacket datagramPacket=new DatagramPacket(packet, packet.length);
                    datagramSocket.receive(datagramPacket);
                    new Handler(datagramPacket).start();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        DatagramSocket datagramSocket=new DatagramSocket(0);
        int port = datagramSocket.getLocalPort();

        Scanner scanner=new Scanner(System.in);
        String najava="NAJAVA@" + port;
        byte[] najavaBytes = najava.getBytes();
        DatagramPacket datagramPacket=new DatagramPacket(najavaBytes,najavaBytes.length, InetAddress.getLocalHost(),11223);
        DatagramSocket socket=new DatagramSocket();
        socket.send(datagramPacket);

        byte[] odgovor=new byte[500];
        DatagramPacket packet=new DatagramPacket(odgovor, odgovor.length);
        socket.receive(packet);

        String id=new String(packet.getData(),0,packet.getLength());

        Thread thread= new Server(datagramSocket);
        thread.start();

        System.out.print("Vnesi 2 za odjava: ");
        int command = Integer.parseInt(scanner.nextLine());
        if(command==2){
            String odjava="ODJAVA@" + id;
            byte[] odjavaBytes = odjava.getBytes();
            DatagramPacket packet1=new DatagramPacket(odjavaBytes,odjavaBytes.length, InetAddress.getLocalHost(), 11223);
            DatagramSocket socket1=new DatagramSocket();
            socket1.send(packet1);

            byte[] odgovor1=new byte[500];
            DatagramPacket packet2=new DatagramPacket(odgovor1,odgovor1.length);
            socket1.receive(packet2);

            String rsp=new String(packet2.getData(),0,packet2.getLength());
            System.out.println(rsp);
            datagramSocket.close();
        }




    }
}
