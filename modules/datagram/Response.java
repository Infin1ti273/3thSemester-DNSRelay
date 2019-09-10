package modules.datagram;

import java.util.Vector;

public class Response extends DatagramBuilder {

    private byte[] name;    //cursor
    private byte[] type;
    private byte[] className;
    private byte[] ttl;
    private byte[] rdLength;
    private byte[] rData;

    public Response(String[] name, String[] type, String[] className, String[] ttl, String[] rdLength, String[] rData) {
        this.name = strings2bytes(name);
        this.type = strings2bytes(type);
        this.className = strings2bytes(className);
        this.ttl = strings2bytes(ttl);
        this.rdLength = strings2bytes(rdLength);
        this.rData = strings2bytes(rData);
    }

    byte[] build() {
        byte[] b1 = bytesConcat(this.name, this.type);
        byte[] b2 = bytesConcat(this.className, this.ttl);
        byte[] b3 = bytesConcat(this.rdLength, this.rData);
        return bytesConcat(bytesConcat(b1, b2),b3);
    }

    void output() {
        Vector<String> vector = new Vector<>();
        for (byte b: build()) {
            vector.add(Integer.toHexString(b & 0xFF));
        }
        System.out.println("#Response");
        for (String s: vector) {
            System.out.print(s + " ");
        }
        System.out.println();
    }
}
