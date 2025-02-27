package backend.globalvars;

public class GlobalBoolean extends GlobalVar<Boolean>{
    public GlobalBoolean(String playerSpacePath, String clientName, String name){
        super(playerSpacePath, clientName, name, Boolean::parseBoolean);
    }
    public GlobalBoolean(String playerSpacePath, String clientName, String name, Boolean value){
        super(playerSpacePath, clientName, name, Boolean::parseBoolean, value);
    }
}
