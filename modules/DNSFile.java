import java.io.*;
import java.util.Hashtable;

/**
 * 读取文件中的域名:IP对
 */
class DNSFile {
    private static final String FILE_DIRECTION = "./DNSRelay.txt";

    /**
     * 如果文件不存在或者文件为空，则返回空的hashtable
     *
     * @return 域名:IP对
     */
    static Hashtable<String, String> load() throws IOException {
        File file = new File(FILE_DIRECTION);
        Hashtable<String, String> result = new Hashtable<>();
        String line;

        if (!file.exists()) {
            System.out.println("Error: File not exist!");
            return result;
        } else {
            System.out.println("Loading File......");
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            while ((line = bf.readLine()) != null) {
                String[] li = line.split(" ");
                result.put(li[1], li[0]);
            }
            bf.close();
            return result;
        }
    }
}
