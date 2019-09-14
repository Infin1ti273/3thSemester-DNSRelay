package modules;

import modules.datagram.DNSDatagram;
import modules.datagram.Response;

import java.io.IOException;
import java.net.*;

/**
 * 修改报文header，增加response部分并发送
 */
class ForwardToLocal implements IForwardType {
    private DNSDatagram dnsDatagram;

    /**
     * 构造并发送报文
     *
     */
    @Override
    public void sendPkg(DatagramPacket datagramPacket, DNSDatagram dnsDatagram) {
        int clientPort = datagramPacket.getPort();
        InetAddress clientAddress = datagramPacket.getAddress();
        this.dnsDatagram = dnsDatagram;

        changeFlag();
        changeAnswerCount();
        addResponseData();
        DatagramPacket responsePacket = new DatagramPacket(new byte[4096], 4096);
        responsePacket.setPort(clientPort);
        responsePacket.setAddress(clientAddress);
        responsePacket.setData(this.dnsDatagram.build());

        synchronized (Listen.Lock) {
            try {
                Listen.localSocket.send(responsePacket);
                System.out.println(Thread.currentThread().getName() + "匹配请求的回应已发送至本机:");
                dnsDatagram.debugOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 先根据ip地址，修改原报文中的flag
     */
    private void changeFlag() {
        String[] flag = new String[2];
        if (DNSFile.search(dnsDatagram.getRequest().extractName()).equals("0.0.0.0")) {
            flag[0] = "81";
            flag[1] = "83";
        } else {
            flag[0] = "81";
            flag[1] = "80";
        }

        dnsDatagram.getHeader().setFlags(flag);
    }

    /**
     * 修改Response中的回答数
     */
    private void changeAnswerCount() {
        dnsDatagram.getHeader().setAnCount(new String[]{"0", "1"});
    }

    /**
     * 向报文中添加Response信息
     */
    private void addResponseData() {
        String[] cursor = {"c0", "0c"};     //代表域名开始的位置
        String[] type = {"00", "01"};       //A
        String[] className = {"00", "01"};  //IN
        String[] timeToLive = {"00", "01", "51", "80"};     //取决于服务器，此处为默认设置
        String[] data = ipString2hexString(DNSFile.search(dnsDatagram.getRequest().extractName()));    //数据（ip）
        String[] dataLength = new String[]{"00",Integer.toHexString(data.length)};      //数据长度

        Response response = new Response(cursor, type, className, timeToLive, dataLength, data);
        this.dnsDatagram = new DNSDatagram(this.dnsDatagram.getHeader(), this.dnsDatagram.getRequest(), response);
    }

    /**
     * 将ip转换为16进制string数组
     *
     * @param host 目标ip
     * @return ip的16进制string数组
     */
    private String[] ipString2hexString(String host) {
        String[] ipSegment = host.split("\\.");
        for (int i = 0; i < ipSegment.length; i++) {
            ipSegment[i] = Integer.toHexString(Integer.parseInt(ipSegment[i]));
        }
        return ipSegment;
    }

}
