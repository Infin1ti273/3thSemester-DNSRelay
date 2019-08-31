import java.io.*;
import java.util.Hashtable;

/**
 * 读取文件中的域名:IP对
 */
class DNSFile {
    private static final String FILE_DIRECTION = "./DNSRelay.txt";

    /**
     * 寻找文件中是否有匹配的ip:
     */
    static String[] searchPair(String request) throws IOException {
        for (int i = 0; i < load().size(); i++) {
            String[] pair = load().get(i);
           if (request.equals(pair[0]) || request.equals(pair[1])) {
               return pair;
           }
        }
        return null;
    }

    /**
     * 获取host表
     * 如果文件不存在或者文件为空，则返回空的hashtable
     *
     * @return 域名:IP对
     */
    private static Hashtable<Integer, String[]> load() throws IOException {
        File file = new File(FILE_DIRECTION);
        Hashtable<Integer, String[]> result = new Hashtable<>();
        String line;

        if (!file.exists()) {
            System.out.println("Error: File not exist!");
            return result;
        } else {
            System.out.println("Loading File......");
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            int i = 0;
            while ((line = bf.readLine()) != null) {
                String[] li = line.split(" ");
                result.put(i++, li);
            }
            bf.close();
            return result;
        }
    }
}
