package backend.globalvars;

import backend.Session;

public class GlobalString extends GlobalVar<String>{
    private static final int BACKSLASH = 1;
    private static final int SLASH = 2;
    private static final int COLON = 3;
    private static final int ASTERISK = 4;
    private static final int QUESTION_MARK = 5;
    private static final int QUOTE = 6;
    private static final int LESS = 7;
    private static final int GREATER = 8;
    private static final int PIPE = 9;
    private static final String ESCAPE = "#";
    public GlobalString(Session session, String name){
        super(session, name, String::valueOf);
    }
    public GlobalString(Session session, String clientName, String name, String value){
        super(session,name, String::valueOf, value);
    }
    @Override
    public void setValue(String value) {
        value.replaceAll(ESCAPE, ESCAPE+ESCAPE);
        value.replaceAll("\\\\", ESCAPE + BACKSLASH);
        value.replaceAll("/", ESCAPE + SLASH);
        value.replaceAll(":", ESCAPE + COLON);
        value.replaceAll("\\*", ESCAPE + ASTERISK);
        value.replaceAll("\\?", ESCAPE + QUESTION_MARK);
        value.replaceAll("\"", ESCAPE + QUOTE);
        value.replaceAll("<", ESCAPE + LESS);
        value.replaceAll(">", ESCAPE + GREATER);
        value.replaceAll("\\|", ESCAPE + PIPE);
        super.setValue(value);
    }

    @Override
    public String getValue() {
        String value = super.getValue();
        value.replaceAll(ESCAPE+ESCAPE, ESCAPE);
        value.replaceAll(ESCAPE + BACKSLASH, "\\");
        value.replaceAll(ESCAPE + SLASH, "/");
        value.replaceAll(ESCAPE + COLON, ":");
        value.replaceAll(ESCAPE + ASTERISK, "*");
        value.replaceAll(ESCAPE + QUESTION_MARK, "?");
        value.replaceAll(ESCAPE + QUOTE, "\"");
        value.replaceAll(ESCAPE + LESS, "<");
        value.replaceAll(ESCAPE + GREATER, ">");
        value.replaceAll(ESCAPE + PIPE, "|");
        return value;
    }

}
