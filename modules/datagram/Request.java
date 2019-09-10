package modules.datagram;

import java.util.Vector;

public class Request extends DatagramBuilder {

    private byte[] qName;
    private byte[] qType;
    private byte[] qClass;

    public Request(byte[] qName, byte[] qType, byte[] qClass) {
        this.qName = qName;
        this.qType = qType;
        this.qClass = qClass;
    }

    /**
     * 提取域名(可能是ip)，并将域名数组翻译为实际域名（字符串）
     * @return 域名字符串
     */
    public String extractName() {

        Vector<String> name = new Vector<>(0, 1);
        for (byte b : this.qName) {
            name.add(Integer.toHexString(b));
        }
        StringBuilder nameReturn = new StringBuilder();

        //取域名中的数N，并转换后N个数
        for (int i = 0; i < name.size(); i++) {
            //除了第一个数/0值以外，其余N值替换为点号
            if (i != 0 && !name.get(i).equals("0")) {
                nameReturn.append(".");
            }
            //转换指定数量的数
            for (int j = Integer.parseInt(name.get(i)); j > 0; j--,i++) {
                nameReturn.append((char) Integer.parseInt(name.get(i + 1), 16));
            }
        }
        return String.valueOf(nameReturn);
    }

    public byte[] getqName() {
        return qName;
    }

    public byte[] getqType() {
        return qType;
    }

    public byte[] getqClass() {
        return qClass;
    }

    byte[] build() {
        return bytesConcat(bytesConcat(qName, qType),qClass);
    }

    //输出请求
    void output() {
        Vector<String> name = new Vector<>(0, 1);

        System.out.println("#Request:");
        for (byte b: this.qName) {
            name.add(Integer.toHexString(b  & 0xFF));
        }
        //输出域名
        for (String s: name) {
            System.out.print(s + " ");
        }
        System.out.println("0");
        System.out.println("实际域名: " + extractName());

        //输出剩余信息（type和class）
        System.out.println("Type: " + Integer.toHexString(qType[0]) + " " + Integer.toHexString(qType[1]));
        System.out.println("Class: " + Integer.toHexString(qClass[0]) + " " + Integer.toHexString(qClass[1]) + "\n");
    }


}
