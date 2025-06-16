import java.io.File;

public class Debug {
    public static void main(String[] args) {
        File all = new File("C:\\Users\\natha\\Downloads\\testServers");
        deleteContents(all);
    }

    private static void deleteContents(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                deleteContents(f);
            }
            f.delete();
        }
    }
}
