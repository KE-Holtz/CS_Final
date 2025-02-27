package backend.globalvars;

public class GlobalString extends GlobalVar<String>{
    public GlobalString(String playerSpacePath, String clientName, String name){
        super(playerSpacePath, clientName, name, String::valueOf);
    }
    public GlobalString(String playerSpacePath, String clientName, String name, String value){
        super(playerSpacePath, clientName, name, String::valueOf, value);
    }
    //TODO: override setValue to make sure the string is valid and can be written to the folder name
}
