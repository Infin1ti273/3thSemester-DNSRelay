import java.io.IOException;


import modules.DNSFile;
import modules.Listen;

public class Program {

    public static void main(String[] args) throws IOException {
        //启动文件模块，如果没有文件直接退出程序
        new DNSFile();
        if (DNSFile.isTableNull()) {
            return;
        }
        //启动监听模块
        new Listen();
    }

}
