import java.net.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 监听本机发送的dns请求并进行处理
 */
public class Listen implements Runnable {

    private InetAddress localhost;
    private DatagramSocket datagramSocket;
    private byte[] receive = new byte[1024];


    /**
     * 初始化监听配置并创建线程
     */
    Listen() {
        try {
            localhost = InetAddress.getLocalHost();
            datagramSocket = new DatagramSocket(53, localhost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Initiating listening module...");
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * 监听进程，尝试抓取报文并进行分析，之后进行请求回应
     * Capture the diagram, analyze them, then forward/response them
     */
    @Override
    public void run() {

        DatagramPacket pkgReceive = new DatagramPacket(receive, receive.length);
        while (true) {
            try {
                //尝试抓取报文
                datagramSocket.receive(pkgReceive);
                //解析报文，转为字符数组
                Vector<String> pkgArr = Analyze.translatePkg(pkgReceive.getData());

                //如果匹配DNSFile则发送给本地
                IForwardType forwardType = (DNSFile.searchPair(Analyze.extractName(pkgArr)) == null)?
                        new ForwardToInternet(): new ForwardToLocal();
                Forward f = new Forward(forwardType, localhost.toString());
                f.sendPkg(pkgArr);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
