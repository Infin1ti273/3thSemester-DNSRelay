package modules;

import modules.datagram.DNSDatagram;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监听本机发送的dns请求并进行处理
 */
public class Listen {
    static DatagramSocket localSocket;

    static final Object Lock = new Object();

    /**
     * 初始化监听配置并创建线程
     */
    public Listen() throws IOException {
        localSocket = new DatagramSocket(53, InetAddress.getByName("127.0.0.1"));
        DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);

        ExecutorService servicePool = Executors.newFixedThreadPool(10);

        while (true) {
            try {
                localSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            servicePool.execute(new Analyze(receivePacket));
        }
    }


}
