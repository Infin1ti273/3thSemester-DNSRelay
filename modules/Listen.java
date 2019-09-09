package modules;

import modules.datagram.DNSDatagram;

import java.net.*;
import java.io.*;

/**
 * 监听本机发送的dns请求并进行处理
 */
public class Listen implements Runnable {
    static DatagramSocket datagramSocket;

    static {
        try {
            datagramSocket = new DatagramSocket(53, InetAddress.getByName("127.0.0.1"));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private byte[] receive = new byte[1024];

    /**
     * 初始化监听配置并创建线程
     */
    public Listen() throws UnknownHostException {

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
                DNSDatagram dnsDatagram = Analyze.translatePkg(pkgReceive.getData());


                String[] flags = new String[]{
                        Integer.toHexString(dnsDatagram.getHeader().getFlags()[0]),
                        Integer.toHexString(dnsDatagram.getHeader().getFlags()[1])
                };
                //如果Flag是8180，直接发回电脑
                if (flags[0].equals("81") && flags[1].equals("80")) {
                    ForwardResponse fr = new ForwardResponse();
                    fr.sendPkg(pkgReceive.getData());
                    System.out.println("网络/本机回复：");
                }
                else {
                    //只接收A类请求和0100的情况
                    if (Integer.toHexString(dnsDatagram.getRequest().getqType()[1]).equals("1") &&
                            flags[0].equals("1") &&
                            flags[1].equals("0")) {
                        //如果匹配DNSFile则发送给本地
                        if (DNSFile.search(dnsDatagram.getRequest().extractName()) != null) {
                            Forward f = new Forward(new ForwardToLocal());
                            f.sendPkg(pkgReceive, dnsDatagram);
                            System.out.println("匹配请求：");
                        }
                        else {
                            Forward f = new Forward(new ForwardToInternet());
                            f.sendPkg(pkgReceive, dnsDatagram);
                            System.out.println("不匹配请求：");
                        }
                    }
                    else {
                        System.out.println("非A类请求/0100，不进行发送：");
                    }
                }
                /*TODO*/ //debug
                dnsDatagram.debugOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
