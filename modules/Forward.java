import java.util.Vector;

/**
 * 构造报文并发送给公网/本机
 */
class Forward {
    private static String remoteDNS;
    private IForwardType iForwardType;
    private String host;

    /**
     * 配置发送类型和发送目标ip
     * @param iForwardType 发送类型
     * @param host 目标ip
     */
    Forward(IForwardType iForwardType, String host) {
        this.iForwardType = iForwardType;
        this.host = host;
    }

    /**
     * 构造数据报并进行发送
     * @param pkgArr 接收到的数据报
     */
    void sendPkg(Vector<String> pkgArr) {
        this.iForwardType.sendPkg(pkgArr, this.host);
    }
}
