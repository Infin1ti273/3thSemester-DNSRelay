package modules;

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
        localSocket = new DatagramSocket(53);

        System.out.println("Initiating Listening module......");

        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        //noinspection InfiniteLoopStatement
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(new byte[4096], 4096);
            try {
                localSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            threadPool.execute(new Analyze(receivePacket));
        }
    }


}
