package modules;

import modules.datagram.DNSDatagram;

import java.net.DatagramPacket;


/**
 * 构造报文并发送给公网/本机
 */
class Forward {
    private IForwardType iForwardType;

    /**
     * 配置发送类型和发送目标ip
     * @param iForwardType 发送类型
     */
    Forward(IForwardType iForwardType) {
        this.iForwardType = iForwardType;
    }

    /**
     * 构造数据报并进行发送
     * @param dnsDatagram 接收到的数据报
     */
    void sendPkg(DatagramPacket datagramPacket, DNSDatagram dnsDatagram) {
        this.iForwardType.sendPkg(datagramPacket, dnsDatagram);
    }
}
