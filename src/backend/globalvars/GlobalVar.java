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

    public static final int MAX_LENGTH = 255;

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
        setValue(valueParser.apply("0"), Tag.DEFAULT);
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
                if (tags.contains(Tag.OVERFLOW)) {
                    value = valueParser.apply(valueOf(readOverflow(instance)));
                } else {
                    value = valueParser.apply(valueOf(instance.getName()));
                }
                newestTime = instance.lastModified();
            }
        }
        return value;
    }

    public String readOverflow(File file){
        ArrayList<Tag> tags = getTags(file.getName());
        if (tags.contains(Tag.OVERFLOW)){
            return valueOf(file.getName()) + readOverflow(file.listFiles()[0]);
        } else {
            return valueOf(file.getName());
        }
    }

    public void writeOverflow(File parent, String value){
        String tag = "()";
        if (tag.length() + value.toString().length() > MAX_LENGTH){
            tag = tag.substring(tag.length()-1) + Tag.OVERFLOW + ")";
        }
        if (tag.contains(Tag.OVERFLOW.toString())) {
            File newFile = new File(parent.getPath() + "\\" + tag + value.toString().substring(0,MAX_LENGTH - tag.length()));
            writeOverflow(newFile, value);
        } else {
            File newFile = new File(parent.getPath() + "\\" + tag + value);
        }
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
        setValue(value, null);
    }

    public void setValue(T value, Tag... tags) {
        deleteContents(varFile);
        String tag = "(";
        for (Tag t : tags) {
            tag += t + ",";
        }
        tag += ")";

        if (tag.length() + value.toString().length() > MAX_LENGTH && !tag.contains(Tag.OVERFLOW.toString())){
            tag = tag.substring(tag.length()-1) + Tag.OVERFLOW + ")";
        }

        if(tag.contains(Tag.OVERFLOW.toString())){
            File newFile = new File(varFile.getPath() + "\\" + tag + value.toString().substring(0, MAX_LENGTH - tag.length()));
            writeOverflow(newFile, value.toString().substring(MAX_LENGTH-tag.length()));
            newFile.mkdir();
        } else{
            File newFile = new File(varFile.getPath() + "\\" + tag + value);
            newFile.mkdir();
        }
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

    private String valueOf(String value) {
        return value.substring(value.indexOf(")") + 1);
    }
}
