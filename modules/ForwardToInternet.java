package modules;

import modules.datagram.DNSDatagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


class ForwardToInternet implements IForwardType {

    /**
     * 发送报文至网络
     */
    @Override
    public void sendPkg(DatagramPacket datagramPacket, DNSDatagram dnsDatagram){
        try {
            DatagramPacket responsePacket = new DatagramPacket(new byte[1024], 1024);
            responsePacket.setPort(53);
            responsePacket.setAddress(InetAddress.getByName("10.3.9.4"));
            responsePacket.setData(dnsDatagram.build());
            DatagramSocket internetSocket = new DatagramSocket();
            internetSocket.send(datagramPacket);
            System.out.println("不匹配请求已发送至网络");

            DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
            internetSocket.receive(receivePacket);
            internetSocket.close();
            Listen.datagramSocket.send(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
