package modules;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * 将请求原封不动发给主机
 */
class ForwardResponse{

    ForwardResponse() {

    }

    /**
     * 直接发送报文
     *
     */
    void sendPkg(byte[] data) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(new byte[1024], 1024);
            datagramPacket.setPort(53);
            datagramPacket.setAddress(InetAddress.getByName("127.0.0.1"));
            datagramPacket.setData(data);
            Listen.datagramSocket.send(datagramPacket);
            System.out.println("已发送远程回应");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
