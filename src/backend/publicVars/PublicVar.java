package backend.publicVars;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import backend.Session;
import gameplay.Player;

public class PublicVar<T> {

    private final String playerName;

    private final String name;
    private final File   varFile;

    private final Function<String, T> valueParser;

    public static final int MAX_LENGTH = 160;

    public enum Tag {
        DEFAULT("DEFAULT"),
        OVERFLOW("OVERFLOW");

        private final String tag;

        Tag(String tag) {
            this.tag = tag;
        }
    }

    public PublicVar(Player player, String name, Function<String, T> valueParser) {
        playerName = player.getName();
        this.name = name;
        this.varFile =
                new File(player.getPlayerFolder() + "\\" + "publicVars" + "\\" + name);
        this.valueParser = valueParser;
        if (!varFile.mkdir()) {
            System.out.println("[DEBUG] " + name + " Failed");
        }
        setValue(valueParser.apply("0"), Tag.DEFAULT);
    }

    public PublicVar(Player player, String name, Function<String, T> valueParser, T value) {
        playerName = player.getName();
        this.name = name;
        this.varFile =
                new File(player.getPlayerFolder() + "\\" + "publicVars" + "\\" + name);
        this.valueParser = valueParser;
        if (!varFile.mkdir()) {
            System.out.println("[DEBUG] " + name + " Failed");
        }
        setValue(value);
    }

    // ? Returns null if no value is found - is this ok?

    public T getValue() {
        String value = varFile.list()[0];
        System.out.println(value);
        ArrayList<Tag> tags = getTags(value);
        if (tags.contains(Tag.OVERFLOW)) {
            return valueParser.apply(readOverflow(varFile));
        } else {
            return valueParser.apply(valueOf(value));
        }
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
            if (tag.length() + currentValue.toString()
                                           .length()
                    + 1 > MAX_LENGTH) {
                tag += Tag.OVERFLOW + ")";
            } else {
                tag += ")";
            }

            File nextFile;
            if (tag.contains(Tag.OVERFLOW.toString())) {
                nextFile = new File(currentParent.getAbsolutePath() + "\\" + tag
                        + currentValue.substring(0, MAX_LENGTH - tag.length()));
                currentParent = nextFile;
                currentValue = currentValue.substring(MAX_LENGTH - tag.length());
                System.out.println(nextFile.getName());
                System.out.println(nextFile.mkdir());
            } else {
                nextFile = new File(currentParent.getAbsolutePath() + "\\" + tag + currentValue);
                System.out.println(nextFile.getName());
                currentValue = "";
                System.out.println(nextFile.mkdir());
            }
        }
    }

    public ArrayList<Tag> getTags(String str) {
        String tagStr = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
        ArrayList<Tag> tags = new ArrayList<>();
        for (String s : tagStr.split(",")) {
            if (!s.isEmpty()) {
                tags.add(Tag.valueOf(s));
            }
        }
        return tags;
    }

    public void setValue(T value) {
        setValue(value, null);
    }

    public void setValue(T value, Tag... tags) {
        deleteContents(varFile);

        String tag = "(";
        if (tags != null) {
            for (Tag t : tags) {
                tag += t + ",";
            }
        }
        if (tag.length() + value.toString()
                                .length()
                > MAX_LENGTH && !tag.contains(Tag.OVERFLOW.toString())) {
            tag = tag.substring(tag.length() - 1) + Tag.OVERFLOW + ",";
        }
        tag += ")";
        if (tag.contains(Tag.OVERFLOW.toString())) {
            File newFile = new File(
                                    varFile.getPath() + "\\" + tag + value.toString()
                                                                          .substring(0, MAX_LENGTH
                                                                                  - tag.length()));
            newFile.mkdir();
            writeOverflow(newFile, value.toString()
                                        .substring(MAX_LENGTH - tag.length()));
        } else {
            File newFile = new File(varFile.getPath() + "\\" + tag + value);
            newFile.mkdir();
        }
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

    public String getName() {
        return name;
    }
}
