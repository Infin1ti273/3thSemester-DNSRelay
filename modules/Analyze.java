package modules;

import modules.datagram.DNSDatagram;
import modules.datagram.Header;
import modules.datagram.Request;

import java.util.Arrays;

/**
 * 解析报文信息
 */
class Analyze {
    private static final int NAME_FIRST_INDEX = 12;     //报文中域名所在的第一位

    /**
     * 提取报文中的信息，将其转换为完整报文的数组，并暂存至程序
     * @param pkg 报文信息（二进制数）
     * @return 解析完成的报文类
     */
    static DNSDatagram translatePkg(byte[] pkg) {

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


}
