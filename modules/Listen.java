import java.net.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 监听本机发送的dns请求并进行处理
 */
public class Listen implements Runnable {

    private Hashtable<String, String> DNSTable;
    private DatagramSocket datagramSocket;
    private byte[] receive = new byte[1024];
    private byte[] send = new byte[1024];
    private static final int NAME_FIRST_INDEX = 12;     //报文中域名所在的第一位

//    private String threadName;

    /**
     * 初始化监听配置并创建线程
     * @param DNSTable 从文件中获取的 域名:IP
     */
    Listen(Hashtable<String, String> DNSTable) {
        InetAddress host = null;

        this.DNSTable = DNSTable;
        try {
            host = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            datagramSocket = new DatagramSocket(53, host);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Initiating listening module...");
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * 监听进程，尝试抓取报文并进行分析
     * Capture the diagram, analyze them, then forward/response them
     */
    @Override
    public void run() {

        DatagramPacket pkgReceive = new DatagramPacket(receive, receive.length);
        while (true) {
            try {
                //尝试抓取报文
                datagramSocket.receive(pkgReceive);
                analysePkg(pkgReceive.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提取报文中的信息，将其转换为完整报文的数组，和域名数组
     * @param pkg 报文信息（二进制数）
     */
    private void analysePkg(byte[] pkg) {
        Vector<String> pkgArr = new Vector<>(0, 1);
        Vector<String> name = new Vector<>(0, 1);

        for (byte b : pkg) {
            pkgArr.add(Integer.toHexString(b & 0xFF));
        }
        System.out.print("Name: ");
        for (int i = NAME_FIRST_INDEX; !pkgArr.get(i).equals("0"); i++) {
            name.add(pkgArr.get(i));
        }
        pkgDebugOutput(pkgArr, name);
    }

    /**
     * 将域名数组翻译为实际域名（字符串）
     * @param name 域名数组
     * @return 域名字符串
     */
    private String nameTranslate(Vector<String> name) {
        StringBuilder nameReturn = new StringBuilder();

        //取域名中的数N，并转换后N个数
        for (int i = 0; i < name.size(); i++) {
            //除了第一个数以外，其余N值替换为点号
            if (i != 0) {
                nameReturn.append(".");
            }
            //转换指定数量的数
            for (int j = Integer.parseInt(name.get(i)); j > 0; j--,i++) {
                nameReturn.append((char) Integer.parseInt(name.get(i + 1), 16));
            }
        }
        return String.valueOf(nameReturn);
    }

    /**
     * Debug用
     * 输出程序获取到的报文信息
     * @param pkgArr 完整的报文信息（String数组）
     * @param name 域名信息（String数组，没有去掉符号）
     */
    private void pkgDebugOutput(Vector<String> pkgArr, Vector<String> name) {

        //输出header
        System.out.println(
                "Receive: \n" +
                        "#Header \n" +
                        "ID: " + pkgArr.get(0) + " " + pkgArr.get(1) + "\n" +
                        "Flag: " + pkgArr.get(2) + " " + pkgArr.get(3) + "\n" +
                        "QDCOUNT: " + pkgArr.get(4) + " " + pkgArr.get(5) +
                        " ANCOUNT: " + pkgArr.get(6) + " " + pkgArr.get(7) +
                        " NSCOUNT: " + pkgArr.get(8) + " " + pkgArr.get(9) +
                        " ARCOUNT: " + pkgArr.get(10) + " " + pkgArr.get(11) + "\n" +
                        "\n" +
                        "#Question"
        );

        //输出报文
        for (String s: name) {
            System.out.print(s + " ");
        }
        System.out.println("0");
        System.out.println("Name: " + nameTranslate(name));

        //输出剩余信息（type和class）
        int i = NAME_FIRST_INDEX + name.size() + 1; //报文结束的后一位
        System.out.println("Type: " + pkgArr.get(i + 1) + " " + pkgArr.get(i + 2));
        System.out.println("Class: " + pkgArr.get(i + 3) + " " + pkgArr.get(i + 4) + "\n");
    }
}
