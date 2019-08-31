import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //启动监听模块
        new Listen(DNSFile.load());
        //启动发送模块
        new Forward();
    }
}
