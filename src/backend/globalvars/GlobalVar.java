package backend.globalvars;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;

public class GlobalVar<T> {

    private final String playerSpacePath;
    private final File   playerSpaceFolder;

    private final String clientName;

    private final String name;
    private final File varFile;

    private final Function<String, T> valueParser;

    public GlobalVar(String playerSpacePath, String clientName, String name, Function<String, T> valueParser) {
        this.playerSpacePath = playerSpacePath;
        this.playerSpaceFolder = new File(playerSpacePath);

        this.clientName = clientName;

        this.name = name;
        this.varFile = new File(playerSpacePath + "\\" + clientName + "\\" + "globalVars" + "\\" + name);

        this.valueParser = valueParser;

        varFile.mkdir();
        setValue(valueParser.apply("0"), "default");
    }

    public GlobalVar(String playerSpacePath, String clientName, String name, Function<String, T> valueParser, T value) {
        this.playerSpacePath = playerSpacePath;
        this.playerSpaceFolder = new File(playerSpacePath);

        this.clientName = clientName;

        this.name = name;
        this.varFile = new File(playerSpacePath + "\\" + clientName + "\\" + "globalVars" + "\\" + name);

        this.valueParser = valueParser;

        varFile.mkdir();
        setValue(value);
    }

    //? Returns null if no value is found - is this ok?
    public T getValue() {
        File[] instances = Stream.of(playerSpaceFolder.listFiles())
                                 .map(x -> x.getPath() + "\\" + "globalVars" + "\\" + name)
                                 .map(File::new)
                                 .toArray(File[]::new);
        long newestTime = Long.MAX_VALUE;
        T value = null;
        for(File instance : instances){
            if(instance.lastModified() < newestTime && instance.exists() && tagOf(instance.getName()).equals("default")){
                newestTime = instance.lastModified();
                value = valueParser.apply(valueOf(instance.getName()));
            }
        }

        return value;
    }

    public void setValue(T value) {
        setValue(value, "");
    }

    public void setValue(T value, String tag){
        deleteContents(varFile);
        File newFile = new File(varFile.getPath() + "\\" + "(" + tag + ")" + value);
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

    private String tagOf(String value){
        return value.substring(1, value.indexOf(")"));
    }

    private String valueOf(String value){
        return value.substring(value.indexOf(")") + 1);
    }
}
