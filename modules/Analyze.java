package modules;

import modules.datagram.DNSDatagram;
import modules.datagram.Header;
import modules.datagram.Request;

import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析报文信息
 */
class Analyze{
    static Map<Integer, Integer> ipMap = new HashMap<>();
    private static final int NAME_FIRST_INDEX = 12;     //报文中域名所在的第一位
    private DatagramPacket receivePacket;

    Analyze(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    /**
     * 对已抓取报文并进行分析，之后进行请求回应
     * Analyze the packet, then forward/response them
     */

    public void run() {
        //解析报文，转为字符数组
        DNSDatagram dnsDatagram = Analyze.translatePkg(receivePacket.getData());
        String[] flags = new String[]{
                Integer.toHexString(dnsDatagram.getHeader().getFlags()[0]),
                Integer.toHexString(dnsDatagram.getHeader().getFlags()[1])
        };
        if (isQuery(receivePacket.getData())) {
                if (DNSFile.search(dnsDatagram.getRequest().extractName()) != null) {
                    //匹配DNSFile,A,发送给本地
                    if (dnsDatagram.getRequest().getqType()[1] != 0x1c) {
                        Forward f = new Forward(new ForwardToLocal());
                        f.sendPkg(receivePacket, dnsDatagram);
                    }
                    //匹配DNSFile,AAAA
                    else {
                        Forward f = new Forward(new ForwardToInternet());
                        f.sendPkg(receivePacket, dnsDatagram);
                    }
                }
                //不匹配DNSFile
                else {
                    Forward f = new Forward(new ForwardToInternet());
                    f.sendPkg(receivePacket, dnsDatagram);
                }
        }
        // response
        else {
            try {
                new ForwardResponse(receivePacket);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
//        else {
//            synchronized (Listen.Lock) {
//                System.out.println(Thread.currentThread().getName() + "收到非A类请求，不进行处理：");
//                /*TODO*/ //debug
//                dnsDatagram.debugOutput();
//            }
//        }
    }



    /**
     * 提取报文中的信息，将其转换为完整报文的数组，并暂存至程序
     * @param pkg 报文信息（二进制数）
     * @return 解析完成的报文类
     */
    private static DNSDatagram translatePkg(byte[] pkg) {

        Header header = new Header(
                new byte[]{pkg[0], pkg[1]},
                new byte[]{pkg[2], pkg[3]},
                new byte[]{pkg[4], pkg[5]},
                new byte[]{pkg[6], pkg[7]},
                new byte[]{pkg[8], pkg[9]},
                new byte[]{pkg[10], pkg[11]});
        int i = NAME_FIRST_INDEX;
        while (pkg[i]!=0) {
            i++;
        }
        byte[] qName = Arrays.copyOfRange(pkg, NAME_FIRST_INDEX, i + 1);
        Request request = new Request(
                qName,
                new byte[]{pkg[i+1], pkg[i+2]},
                new byte[]{pkg[i+3], pkg[i+4]}
        );

        return new DNSDatagram(header, request);
    }

    static int byte2Short( byte[] buf ){

        int targets = (buf[1] & 0xff) | ((buf[0] << 8) & 0xff00); // | 表示安位或
        return targets;
    }


    boolean isQuery(byte[] sendData ){

        return ( ( sendData[2] & 0x80 ) == 0x00 );
    }
}
