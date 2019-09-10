import java.io.IOException;


import modules.DNSFile;
import modules.Listen;

public class DNSRelay {

    public static void main(String[] args) throws IOException {
        //启动文件模块，如果没有文件直接退出程序
        new DNSFile();
        if (DNSFile.isTableNotExist()) {
            System.out.println("警告!没有有效的文件！");
            return;
        }

        //启动监听
        new Listen();
    }

}
