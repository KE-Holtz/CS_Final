package backend.globalvars;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import backend.Session;

public class GlobalVar<T> {

    private final String playerSpacePath;
    private final File   playerSpaceFolder;

    private final String clientName;

    private final String name;
    private final File   varFile;

    private final Function<String, T> valueParser;

    public enum Tag{
        DEFAULT("default"),
        OVERFLOW("overflow");

        private final String tag;
        Tag(String tag){
            this.tag = tag;
        }
    }

    public GlobalVar(Session session, String name, Function<String, T> valueParser) {
        playerSpacePath = session.getPlayerSpacePath();
        playerSpaceFolder = new File(playerSpacePath);

        clientName = session.getClientPlayer()
                            .getName();

        this.name = name;
        this.varFile =
                new File(playerSpacePath + "\\" + clientName + "\\" + "globalVars" + "\\" + name);
        this.valueParser = valueParser;

        varFile.mkdir();
        setValue(valueParser.apply("0"), "default");
    }

    public GlobalVar(Session session, String name, Function<String, T> valueParser, T value) {
        playerSpacePath = session.getPlayerSpacePath();
        playerSpaceFolder = new File(playerSpacePath);

        clientName = session.getClientPlayer()
                            .getName();

        this.name = name;
        this.varFile =
                new File(playerSpacePath + "\\" + clientName + "\\" + "globalVars" + "\\" + name);

        this.valueParser = valueParser;

        varFile.mkdir();
        setValue(value);
    }

    // ? Returns null if no value is found - is this ok?
    public T getValue() {
        File[] values = Stream.of(playerSpaceFolder.listFiles())
                                 .map(x -> x.getPath() + "\\" + "globalVars" + "\\" + name)
                                 .map(File::new)
                                 .map((x) -> x.listFiles()[0])
                                 .toArray(File[]::new);
        long newestTime = Long.MIN_VALUE;
        T value = null;

        ArrayList<Tag> tags;
        for (File instance : values) {
            if (instance.lastModified() > newestTime && instance.exists()) {
                tags = getTags(instance.getName());
                if (tags.contains(Tag.DEFAULT))
                    continue;
                
                newestTime = instance.lastModified();
                value = valueParser.apply(valueOf(instance.getName()));
            }
        }

        return value;
    }

    public ArrayList<Tag> getTags(String str){
        String tagStr = str.substring(str.indexOf("("), str.indexOf(")"));
        ArrayList<Tag> tags = new ArrayList<>();
        for (String s : tagStr.split(",")) {
            tags.add(Tag.valueOf(tagStr));
        }
        return tags;
    }


    public void setValue(T value) {
        setValue(value, "");
    }

    public void setValue(T value, String tag) {
        deleteContents(varFile);
        File newFile = new File(varFile.getPath() + "\\" + "(" + tag + ")" + value);
        newFile.mkdir();
    }

    public void synchronize() {
        setValue(getValue());
    }

    private void deleteContents(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                deleteContents(f);
            }
            f.delete();
        }
    }

    private String tagOf(String value) {
        return value.substring(0, value.indexOf(")"));
    }

    private String valueOf(String value) {
        return value.substring(value.indexOf(")") + 1);
    }
}
