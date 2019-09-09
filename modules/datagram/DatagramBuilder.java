package modules.datagram;

/**
 * 与报文相关的格式转化
 */
abstract class DatagramBuilder {
    /**
     * 将字符串数组转化为byte数组
     * @return 转化后的byte
     */
    static byte[] strings2bytes(String[] strings) {
        byte[] bytes = new byte[strings.length];
        for (int i = 0; i < strings.length;i++) {
            bytes[i] = Integer.valueOf(strings[i], 16).byteValue();
        }
        return bytes;
    }

    /**
     * 合并两个byte数组
     * @param b1 第一个数组
     * @param b2 第二个数组
     * @return 合并后的byte数组
     */
    static byte[] bytesConcat(byte[] b1,byte[] b2){
        byte[] nb = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, nb, 0, b1.length);
        System.arraycopy(b2, 0, nb, b1.length, b2.length);
        return nb;
    }
}
