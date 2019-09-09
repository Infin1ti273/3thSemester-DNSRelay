package modules;

import modules.datagram.DNSDatagram;

import java.io.IOException;
import java.net.DatagramPacket;
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
            Listen.datagramSocket.send(datagramPacket);
            System.out.println("不匹配请求已发送至网络");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
