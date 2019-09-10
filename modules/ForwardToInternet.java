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
            DatagramPacket sendPacket = new DatagramPacket(new byte[1024], 1024);
            sendPacket.setPort(53);
            sendPacket.setAddress(InetAddress.getByName("10.3.9.4"));
            sendPacket.setData(dnsDatagram.build());

            DatagramSocket internetSocket = new DatagramSocket();
            internetSocket.send(sendPacket);
            System.out.println(Thread.currentThread().getName() + "不匹配请求已发送至网络:");
            dnsDatagram.debugOutput();            /*TODO debug*/

            DatagramPacket internetReceivedPacket = new DatagramPacket(new byte[1024], 1024);
            internetSocket.receive(internetReceivedPacket);
            internetSocket.close();

            DatagramPacket responsePacket = new DatagramPacket(internetReceivedPacket.getData(), internetReceivedPacket.getLength());
            responsePacket.setAddress(datagramPacket.getAddress());
            responsePacket.setPort(datagramPacket.getPort());
            synchronized (Listen.Lock) {
                try {
                    Listen.localSocket.send(responsePacket);
                    System.out.println(Thread.currentThread().getName() + "获得来自网络的响应(报文略)");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
