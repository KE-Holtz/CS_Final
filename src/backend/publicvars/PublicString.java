package backend.publicVars;

import gameplay.Player;

public class PublicString extends PublicVar<String> {
    private static final String BACKSLASH = "1";
    private static final String SLASH = "2";
    private static final String COLON = "3";
    private static final String ASTERISK = "4";
    private static final String QUESTION_MARK = "5";
    private static final String QUOTE = "6";
    private static final String LESS = "7";
    private static final String GREATER = "8";
    private static final String PIPE = "9";
    private static final String ESCAPE = "#";

    public PublicString(Player player, String name) {
        super(player, name, String::valueOf);
    }

    public PublicString(Player player, String name, String value) {
        super(player, name, String::valueOf, value);
    }

    @Override
    public void setValue(String value) {
        value = value.replaceAll(ESCAPE, ESCAPE + ESCAPE);
        value = value.replaceAll("\\\\", ESCAPE + BACKSLASH);
        value = value.replaceAll("/", ESCAPE + SLASH);
        value = value.replaceAll(":", ESCAPE + COLON);
        value = value.replaceAll("\\*", ESCAPE + ASTERISK);
        value = value.replaceAll("\\?", ESCAPE + QUESTION_MARK);
        value = value.replaceAll("\"", ESCAPE + QUOTE);
        value = value.replaceAll("<", ESCAPE + LESS);
        value = value.replaceAll(">", ESCAPE + GREATER);
        value = value.replaceAll("\\|", ESCAPE + PIPE);
        super.setValue(value);
    }

    @Override
    public String getValue() {
        String value = super.getValue();
        value = value.replaceAll(ESCAPE + ESCAPE, ESCAPE);
        value = value.replaceAll(ESCAPE + BACKSLASH, "\\\\");
        value = value.replaceAll(ESCAPE + SLASH, "/");
        value = value.replaceAll(ESCAPE + COLON, ":");
        value = value.replaceAll(ESCAPE + ASTERISK, "*");
        value = value.replaceAll(ESCAPE + QUESTION_MARK, "?");
        value = value.replaceAll(ESCAPE + QUOTE, "\"");
        value = value.replaceAll(ESCAPE + LESS, "<");
        value = value.replaceAll(ESCAPE + GREATER, ">");
        value = value.replaceAll(ESCAPE + PIPE, "|");
        return value;
    }

    @Override
    public void setValue(String value, Tag... tags) {
        Tag[] tags2 = new Tag[tags.length + 1];
        for (int i = 0; i < tags.length; i++) {
            tags2[i] = tags[i];
        }
        tags2[tags2.length - 1] = Tag.STRING;
        super.setValue(value, tags);
    }

}
