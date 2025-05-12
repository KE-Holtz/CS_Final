package backend.globalvars;

import backend.Session;

public class GlobalString extends GlobalVar<String>{
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
    public GlobalString(Session session, String name){
        super(session, name, String::valueOf);
    }
    public GlobalString(Session session, String name, String value){
        super(session,name, String::valueOf, value);
    }
    @Override
    public void setValue(String value) {
        value = value.replaceAll(ESCAPE, ESCAPE+ESCAPE);
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
        value = value.replaceAll(ESCAPE+ESCAPE, ESCAPE);
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

}
