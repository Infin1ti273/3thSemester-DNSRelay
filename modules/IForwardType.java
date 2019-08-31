import java.util.Vector;

/**
 * 用于选择数据报的发送类型
 */
interface IForwardType {
    int FLAG_FIRST_INDEX = 2;      //报文中flag所在第一位
    int ANSWER_FIRST_INDEX = 6;    //报文中Answer所在第一位
    //    private static final int NAME_FIRST_INDEX = 12;     //报文中域名所在第一位

    /**
     * 构造并发送报文
     */
    void sendPkg(Vector<String> pkgArr, String host);
}
