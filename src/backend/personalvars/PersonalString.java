package backend.personalvars;

import backend.Session;

public class PersonalString extends PersonalVar<String>{
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
    public PersonalString(Session session, String name){
        super(session, name, String::valueOf);
    }
    public PersonalString(Session session, String clientName, String name, String value){
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
    public ClientVar[] getValue() {
        ClientVar<String>[] value = super.getValue();
        for(ClientVar<String> var : value){
            String varValue = var.getValue();
            varValue = varValue.replaceAll(ESCAPE+ESCAPE, ESCAPE);
            varValue = varValue.replaceAll(ESCAPE + BACKSLASH, "\\\\");
            varValue = varValue.replaceAll(ESCAPE + SLASH, "/");
            varValue = varValue.replaceAll(ESCAPE + COLON, ":");
            varValue = varValue.replaceAll(ESCAPE + ASTERISK, "*");
            varValue = varValue.replaceAll(ESCAPE + QUESTION_MARK, "?");
            varValue = varValue.replaceAll(ESCAPE + QUOTE, "\"");
            varValue = varValue.replaceAll(ESCAPE + LESS, "<");
            varValue = varValue.replaceAll(ESCAPE + GREATER, ">");
            varValue = varValue.replaceAll(ESCAPE + PIPE, "|");
        }
        return value;
    }

}
