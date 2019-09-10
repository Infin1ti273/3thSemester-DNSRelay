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
            DatagramPacket sendPacket = new DatagramPacket(new byte[4096], 4096);
            sendPacket.setPort(53);
            sendPacket.setAddress(InetAddress.getByName("10.3.9.4"));
            sendPacket.setData(dnsDatagram.build());
//            synchronized (Listen.Lock) {
                Listen.localSocket.send(sendPacket);
                Analyze.ipMap.put(Analyze.byte2Short(datagramPacket.getData()),datagramPacket.getPort());
                System.out.println(Thread.currentThread().getName() + "不匹配请求已发送至网络:");
            dnsDatagram.debugOutput();            /*TODO debug*/
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
