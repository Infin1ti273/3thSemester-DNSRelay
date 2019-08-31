/**
 * 构造报文并发送给公网/本机
 */
class Forward {
    private IForwardType iForwardType;

    Forward() {

    }

    void send(IForwardType iForwardType) {
        this.iForwardType = iForwardType;
        iForwardType.sendPkg();
    }
}
