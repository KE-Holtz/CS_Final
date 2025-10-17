package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Config {
    static HashMap<String, String> opts = new HashMap<>();

    public static void init(){
        File config = new File("config.toml");
        if (config.exists()) {
            //We could find or make a proper toml parser but we don't really need it
            Scanner configScanner;
            try {
                System.out.print("scanning");
                configScanner = new Scanner(config);
                while (configScanner.hasNextLine()) {
                    String line = configScanner.nextLine();
                    System.out.println("parsing line:" + line);
                    String[] keyValuePair = line.split("=");
                    opts.put(keyValuePair[0].trim(), keyValuePair[1].trim().replace("\"", ""));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        opts.putIfAbsent("os", "windows");
        opts.putIfAbsent("session_space", "S:\\High School\\WuestC\\Drop Box\\Multiplayer");
        opts.putIfAbsent("delimiter", opts.get("os").equals("windows")?"\\":"/");
    }

    public static String getOS(){
        return opts.get("os");
    }
    public static String getSessionSpace(){
        return opts.get("session_space");
    }
    public static String getDelimiter(){
        return opts.get("delimiter");
    }
}
