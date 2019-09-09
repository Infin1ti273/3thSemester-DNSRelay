package modules;

import modules.datagram.DNSDatagram;

import java.net.DatagramPacket;


/**
 * 用于选择数据报的发送类型
 */
interface IForwardType {

    /**
     * 发送报文
     */
    void sendPkg(DatagramPacket datagramPacket, DNSDatagram dnsDatagram);
}
