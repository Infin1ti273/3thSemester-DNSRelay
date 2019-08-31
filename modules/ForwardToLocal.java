import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Vector;

public class ForwardToLocal implements IForwardType {
    private Vector<String> pkgArr;

    @Override
    public void sendPkg(Vector<String> pkgArr, String host) {
        this.pkgArr = pkgArr;
        changeFlag(host);
        changeAnswerCount();
        addResponseData(host);

        try {
            DatagramSocket datagramSocket = new DatagramSocket(53);
            DatagramPacket datagramPacket = new DatagramPacket(new byte[1024], 1024);
            datagramPacket.setPort(53);
            datagramPacket.setAddress(InetAddress.getLocalHost());
            datagramPacket.setData(convert2datagram(pkgArr));
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
            System.out.println("Response to the pkg.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 先根据ip地址，修改原报文中的flag
     *
     * @param host 目标ip
     */
    private void changeFlag(String host) {
        String[] flag = new String[2];
        if (host.equals("0.0.0.0")) {
            flag[0] = "81";
            flag[1] = "83";
        } else {
            flag[0] = "81";
            flag[1] = "80";
        }

        pkgArr.set(FLAG_FIRST_INDEX, flag[0]);
        pkgArr.set(FLAG_FIRST_INDEX + 1, flag[1]);
    }

    /**
     * 修改Response中的回答数
     */
    private void changeAnswerCount() {
        pkgArr.set(ANSWER_FIRST_INDEX + 1, String.valueOf(1));
    }

    /**
     * 向报文中添加Response信息
     *
     * @param host 目标ip
     */
    private void addResponseData(String host) {
        String[] cursor = {"c0", "0c"};     //代表域名开始的位置
        String[] type = {"00", "05"};       //CNAME
        String[] className = {"00", "01"};  //IN
        String[] timeToLive = {"00", "01", "51", "80"};     //随意设置
        String[] data = ipString2hexString(host);           //数据（ip/域名）
        String dataLength = Integer.toHexString(data.length);   //数据长度

        pkgArr.addAll(Arrays.asList(cursor));
        pkgArr.addAll(Arrays.asList(type));
        pkgArr.addAll(Arrays.asList(className));
        pkgArr.addAll(Arrays.asList(timeToLive));
        pkgArr.add(dataLength);
        pkgArr.addAll(Arrays.asList(data));
    }

    /**
     * 将ip转换为16进制string数组
     *
     * @param host 目标ip
     * @return ip的16进制string数组
     */
    private String[] ipString2hexString(String host) {
        String[] ipSegment = host.split(".");
        for (int i = 0; i < ipSegment.length; i++) {
            ipSegment[i] = Integer.toHexString(Integer.parseInt(ipSegment[i]));
        }
        return ipSegment;
    }


    /**
     * 将数据报数组重新转换为可发送的报文
     *
     * @param pkgArr dns数据报
     * @return 二进制格式的可发送报文
     */
    private byte[] convert2datagram(Vector<String> pkgArr) {
        byte[] pkg = new byte[pkgArr.size()];
        for (int i = 0; i < pkgArr.size(); i++) {
            pkg[i] = Byte.parseByte(pkgArr.get(i), 16);
        }
        return pkg;
    }
}
