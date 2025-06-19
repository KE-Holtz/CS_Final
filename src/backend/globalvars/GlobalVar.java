package backend.globalvars;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import backend.Session;

public class GlobalVar<T> {

    private final String playerSpacePath;
    private final File playerSpaceFolder;
    protected final String delimiter;

    private final String clientName;

    private final String name;
    private final File varFile;

    private final Function<String, T> valueParser;

    public static final int MAX_LENGTH = 160;

    public enum Tag {
        DEFAULT,
        OVERFLOW,
    }

    public GlobalVar(Session session, String name, Function<String, T> valueParser) {
        playerSpacePath = session.getPlayerSpacePath();
        playerSpaceFolder = new File(playerSpacePath);
        delimiter = session.getDelimiter();
        
        clientName = session.getClientPlayer()
                .getName();

        this.name = name;
        this.varFile = new File(playerSpacePath + delimiter + clientName + delimiter + "globalVars" + delimiter + name);
        this.valueParser = valueParser;

        if (!varFile.mkdir()) {
            System.out.println("[DEBUG] " + name + " Failed");
        }
        setValue(valueParser.apply("0"), Tag.DEFAULT);
    }

    public GlobalVar(Session session, String name, Function<String, T> valueParser, T value) {
        playerSpacePath = session.getPlayerSpacePath();
        playerSpaceFolder = new File(playerSpacePath);
        delimiter = session.getDelimiter();

        clientName = session.getClientPlayer()
                .getName();

        this.name = name;
        this.varFile = new File(playerSpacePath + delimiter + clientName + delimiter + "globalVars" + delimiter + name);

        this.valueParser = valueParser;

        if (!varFile.mkdir()) {
            System.out.println("[DEBUG] " + name + " Failed");
        }
        setValue(value);
    }

    public Optional<T> getValue() {
        File[] playerFolders = playerSpaceFolder.listFiles();
        Optional<File>[] values = new Optional[playerFolders.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = Optional.of(playerFolders[i]);
        }
        if (values == null || values.length == 0) {
            // System.out.println("Step 1: null or empty");
        }
        for (int i = 0; i < values.length; i++) {
            File[] content = new File(values[i].get().getPath() + delimiter + "globalVars" + delimiter + name).listFiles();
            if (content.length != 1){
                // System.out.println("issue at:" + values[i].get().getPath() + "\\globalVars\\" + name);
                // System.out.println("Possible folders that store the value: " + content.length);
                values[i] = Optional.empty();
            }else{
            values[i] = Optional.of(content[0]);}
        }
        // System.out.println(playerSpaceFolder.listFiles());
        // values = Stream.of(playerSpaceFolder.listFiles())
        //         .map(x -> x.getPath() + "\\" + "globalVars" + "\\" + name)
        //         .map(File::new)
        //         .map((x) -> x.listFiles()[0])
        //         .toArray(File[]::new);
        long newestTime = Long.MIN_VALUE;
        T value = null;

        ArrayList<Tag> tags;
        for (Optional<File> opt : values) {
            if(opt.isEmpty()){
                continue;
            }
            File instance = opt.get();
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
        return Optional.ofNullable(value);
    }

    public String readOverflow(File file) {
        ArrayList<Tag> tags = getTags(file.getName());
        if (tags.contains(Tag.OVERFLOW)) {
            return valueOf(file.getName()) + readOverflow(file.listFiles()[0]);
        } else {
            return valueOf(file.getName());
        }
    }

    public void writeOverflow(File parent, String value) {
        File currentParent = parent;
        String currentValue = value;
        while (currentValue.length() > 0) {
            String tag = "(";
            if (tag.length() + currentValue.toString().length() + 1 > MAX_LENGTH) {
                tag += Tag.OVERFLOW + ")";
            } else {
                tag += ")";
            }

            File nextFile;
            if (tag.contains(Tag.OVERFLOW.toString())) {
                nextFile = new File(currentParent.getAbsolutePath() + delimiter + tag
                        + currentValue.substring(0, MAX_LENGTH - tag.length()));
                currentParent = nextFile;
                currentValue = currentValue.substring(MAX_LENGTH - tag.length());
                // System.out.println(nextFile.getName());
                // System.out.println(nextFile.mkdir());
                nextFile.mkdir();
            } else {
                nextFile = new File(currentParent.getAbsolutePath() + delimiter + tag + currentValue);
                // System.out.println(nextFile.getName());
                currentValue = "";
                // System.out.println(nextFile.mkdir());
                nextFile.mkdir();
            }
        }
    }

    public ArrayList<Tag> getTags(String str) {
        String tagStr = "";
        try{
        tagStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));} catch (Exception e) {
            System.out.println("ERROR W/ TAGS : " + str);
        }
        ArrayList<Tag> tags = new ArrayList<>();
        for (String s : tagStr.split(",")) {
            if (!s.isEmpty()) {
                tags.add(Tag.valueOf(s));
            }
        }
        return tags;
    }

    public void setValue(T value) {
        setValue(value, new Tag[0]);
    }

    public void setValue(T value, Tag... tags) {
        deleteContents(varFile);
        String tag = "(";
        for (Tag t : tags) {
            tag += t + ",";
        }
        if (tag.length() + (value == null ? "0" : value).toString().length() > MAX_LENGTH
                && !tag.contains(Tag.OVERFLOW.toString())) {
            tag = tag.substring(tag.length() - 1) + Tag.OVERFLOW + ",";
        }
        tag += ")";
        if (tag.contains(Tag.OVERFLOW.toString())) {
            File newFile = new File(
                    varFile.getPath() + delimiter + tag + value.toString().substring(0, MAX_LENGTH - tag.length()));
            if(!newFile.mkdir()){
                System.out.println("Failed to make folder " +  newFile.getPath());
            }
            writeOverflow(newFile, value.toString().substring(MAX_LENGTH - tag.length()));
        } else {
            File newFile = new File(varFile.getPath() + delimiter + tag + (value == null ? "0" : value));
            if(!newFile.mkdir()){
                System.out.println("Failed to make folder " +  newFile.getPath());
            }
        }
    }

    public void synchronize() {
        setValue(getValue().orElse(null));
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
