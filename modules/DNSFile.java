package modules;

import java.io.*;
import java.util.Hashtable;

/**
 * 读取文件中的域名:IP对
 */
public class DNSFile {
    private static final String FILE_DIRECTION = "./DNSRelay.txt";
    private static Hashtable<String, String> table;

    public DNSFile() throws IOException {
        System.out.println("Loading DNS files...");
        File file = new File(FILE_DIRECTION);
        Hashtable<String, String> result = new Hashtable<>();
        String line;

        if (!file.exists()) {
            System.out.println("Error: File not exist!");
            DNSFile.table = null;
        } else {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = bf.readLine()) != null) {
                String[] li = line.split(" ");
                result.put(li[1], li[0]);
            }
            bf.close();
            DNSFile.table = result;
        }
    }

    public static boolean isTableNotExist() {
        return table == null;
    }

    /**
     * 寻找文件中是否有匹配的ip:
     * @param request 域名
     * @return 匹配ip/null
     */
    static String search(String request){
        return table.get(request);
    }

}
