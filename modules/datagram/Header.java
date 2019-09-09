package modules.datagram;

import java.util.Vector;

public class Header extends DatagramBuilder {
    private byte[] transID;
    private byte[] flags;
    private byte[] qdCount;
    private byte[] anCount;
    private byte[] nsCount;
    private byte[] arCount;

    public Header(byte[] transID, byte[] flags, byte[] qdCount, byte[] anCount, byte[] nsCount, byte[] arCount) {
        this.transID = transID;
        this.flags = flags;
        this.qdCount = qdCount;
        this.anCount = anCount;
        this.nsCount = nsCount;
        this.arCount = arCount;
    }

    public byte[] getFlags() {
        return flags;
    }

    public void setFlags(String[] flags) {
        this.flags = strings2bytes(flags);
    }

    public void setAnCount(String[] anCount) {
        this.anCount = strings2bytes(anCount);
    }

    byte[] build() {
        byte[] b1 = bytesConcat(transID, flags);
        byte[] b2 = bytesConcat(qdCount, anCount);
        byte[] b3 = bytesConcat(nsCount, arCount);
        return bytesConcat(bytesConcat(b1, b2), b3);
    }

    //输出header
    void output() {
        Vector<String> header = new Vector<>();
        for (byte b : this.transID) {
            header.add(Integer.toHexString(b));
        }
        for (byte b : this.flags) {
            header.add(Integer.toHexString(b));
        }
        for (byte b : this.qdCount) {
            header.add(Integer.toHexString(b));
        }
        for (byte b : this.anCount) {
            header.add(Integer.toHexString(b));
        }
        for (byte b : this.nsCount) {
            header.add(Integer.toHexString(b));
        }
        for (byte b : this.arCount) {
            header.add(Integer.toHexString(b));
        }
        System.out.println(
                        "#Header \n" +
                        "ID: " + header.get(0) + " " + header.get(1) + "\n" +
                        "Flag: " + header.get(2) + " " + header.get(3) + "\n" +
                        "QDCOUNT: " + header.get(4) + " " + header.get(5) +
                        " ANCOUNT: " + header.get(6) + " " + header.get(7) +
                        " NSCOUNT: " + header.get(8) + " " + header.get(9) +
                        " ARCOUNT: " + header.get(10) + " " + header.get(11) + "\n"
        );
    }
}
