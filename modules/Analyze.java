import java.util.Vector;

/**
 * 解析报文信息
 */
class Analyze {
    private static final int NAME_FIRST_INDEX = 12;     //报文中域名所在的第一位

    /**
     * 提取报文中的信息，将其转换为完整报文的数组，和域名数组
     * @param pkg 报文信息（二进制数）
     * @return 解析完成的报文数组
     */
    static Vector<String> translatePkg(byte[] pkg) {
        Vector<String> pkgArr = new Vector<>(0, 1);

        for (byte b : pkg) {
            pkgArr.add(Integer.toHexString(b));
        }

        pkgDebugOutput(pkgArr);
        return pkgArr;
    }

    /**
     * Debug用
     * 输出程序获取到的报文信息
     * @param pkgArr 完整的报文信息（String数组）
     */
    private static void pkgDebugOutput(Vector<String> pkgArr) {
        Vector<String> name = new Vector<>(0, 1);

        //输出header
        System.out.println(
                "Receive: \n" +
                        "#Header \n" +
                        "ID: " + pkgArr.get(0) + " " + pkgArr.get(1) + "\n" +
                        "Flag: " + pkgArr.get(2) + " " + pkgArr.get(3) + "\n" +
                        "QDCOUNT: " + pkgArr.get(4) + " " + pkgArr.get(5) +
                        " ANCOUNT: " + pkgArr.get(6) + " " + pkgArr.get(7) +
                        " NSCOUNT: " + pkgArr.get(8) + " " + pkgArr.get(9) +
                        " ARCOUNT: " + pkgArr.get(10) + " " + pkgArr.get(11) + "\n" +
                        "\n" +
                        "#Question"
        );

        for (int i = NAME_FIRST_INDEX; !pkgArr.get(i).equals("0"); i++) {
            name.add(pkgArr.get(i));
        }
        //输出域名
        for (String s: name) {
            System.out.print(s + " ");
        }
        System.out.println("0");
        System.out.println(extractName(pkgArr));

        //输出剩余信息（type和class）
        int i = NAME_FIRST_INDEX + name.size() + 1; //报文结束的后一位
        System.out.println("Type: " + pkgArr.get(i + 1) + " " + pkgArr.get(i + 2));
        System.out.println("Class: " + pkgArr.get(i + 3) + " " + pkgArr.get(i + 4) + "\n");
    }


    /**
     * 提取域名(可能是ip)，并将域名数组翻译为实际域名（字符串）
     * @param pkgArr 数据报数组
     * @return 域名字符串
     */
    static String extractName(Vector<String> pkgArr) {
        Vector<String> name = new Vector<>(0, 1);
        StringBuilder nameReturn = new StringBuilder();

        for (int i = NAME_FIRST_INDEX; !pkgArr.get(i).equals("0"); i++) {
            name.add(pkgArr.get(i));
        }
        //取域名中的数N，并转换后N个数
        for (int i = 0; i < name.size(); i++) {
            //除了第一个数以外，其余N值替换为点号
            if (i != 0) {
                nameReturn.append(".");
            }
            //转换指定数量的数
            for (int j = Integer.parseInt(name.get(i)); j > 0; j--,i++) {
                nameReturn.append((char) Integer.parseInt(name.get(i + 1), 16));
            }
        }
        return String.valueOf(nameReturn);
    }


}
