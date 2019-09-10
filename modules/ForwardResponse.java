package modules;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class ForwardResponse {
    ForwardResponse(DatagramPacket datagramPacket) throws UnknownHostException {
        if (Analyze.ipMap.containsKey(Analyze.byte2Short(datagramPacket.getData()))) {
            DatagramPacket internetReceivedPacket = new DatagramPacket(new byte[4096], 4096);
            internetReceivedPacket.setData(datagramPacket.getData());
            internetReceivedPacket.setAddress(InetAddress.getByName("127.0.0.1"));
            internetReceivedPacket.setPort(Analyze.ipMap.get(Analyze.byte2Short(datagramPacket.getData())));
            Analyze.ipMap.remove(Analyze.byte2Short(datagramPacket.getData()));

            synchronized (Listen.Lock) {
                try {
                    Listen.localSocket.send(internetReceivedPacket);
                    System.out.println(Thread.currentThread().getName() + "获得来自网络的响应(报文略)");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
