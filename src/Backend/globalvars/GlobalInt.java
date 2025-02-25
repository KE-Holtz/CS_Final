package backend.globalvars;

import java.io.File;
import java.util.stream.Stream;

public class GlobalInt {

    private final String playerSpacePath;
    private final File   playerSpaceFolder;

    private final String clientName;

    private final String name;
    private final File varFile;
    private int value;

    public GlobalInt(String playerSpacePath, String clientName, String name) {
        this.playerSpacePath = playerSpacePath;
        this.playerSpaceFolder = new File(playerSpacePath);

        this.clientName = clientName;

        this.name = name;
        this.varFile = new File(playerSpacePath + "\\" + clientName + "\\" + "globalVars" + "\\" + name);

        varFile.mkdir();
        synchronize();
    }

    public GlobalInt(String playerSpacePath, String clientName, String name, int value) {
        this.playerSpacePath = playerSpacePath;
        this.playerSpaceFolder = new File(playerSpacePath);

        this.clientName = clientName;

        this.name = name;
        this.varFile = new File(playerSpacePath + "\\" + clientName + "\\" + "globalVars" + "\\" + name);

        varFile.mkdir();
        setValue(value);
    }

    public int getValue() {
        File[] instances = Stream.of(playerSpaceFolder.listFiles())
                                 .map(x -> x.getPath() + "\\" + "globalVars" + "\\" + name)
                                 .map(File::new)
                                 .toArray(File[]::new);
        long newest = Long.MAX_VALUE;
        int value = 0;
        for(File instance : instances){
            if(instance.lastModified() < newest && instance.exists()){
                newest = instance.lastModified();
                value = Integer.parseInt(instance.getName());
            }
        }

        return value;
    }

    public void setValue(int value) {
        deleteContents(varFile);
        File newFile = new File(varFile.getPath() + "\\" + value);
    }

    public void synchronize() {
        setValue(getValue());
    }

    private void deleteContents(File file){
        for(File f : file.listFiles()){
            if(f.isDirectory()){
                deleteContents(f);
            }
            f.delete();
        }
    }
}
